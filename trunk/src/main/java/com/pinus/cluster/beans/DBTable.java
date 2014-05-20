package com.pinus.cluster.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.pinus.util.StringUtils;

/**
 * 数据库表bean. 对数据库表的抽象.
 * 
 * @author duanbn
 */
public class DBTable {

	/**
	 * 日志.
	 */
	public static final Logger LOG = Logger.getLogger(DBTable.class);

	/**
	 * 表所在的集群.
	 */
	private String cluster;

	/**
	 * 表名
	 */
	private String name;

	/**
	 * 表下标.
	 */
	private int tableIndex = -1;

	/**
	 * 分表数
	 */
	private int shardingNum;

	/**
	 * 表中的列.
	 */
	private List<DBTableColumn> columns = new ArrayList<DBTableColumn>();

	/**
	 * 包含的索引.
	 */
	private List<DBIndex> indexes = new ArrayList<DBIndex>();

	/**
	 * 构造方法
	 * 
	 * @param name
	 *            表名
	 */
	public DBTable(String name) {
		this.name = name;
	}

	public String getNameWithIndex() {
		if (tableIndex > -1) {
			return this.name + tableIndex;
		}
		return name;
	}

	public void addColumn(DBTableColumn column) {
		this.columns.add(column);
	}

	public void addIndex(DBIndex index) {
		this.indexes.add(index);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DBTableColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<DBTableColumn> columns) {
		this.columns = columns;
	}

	public Map<String, DBTableColumn> getColumnMap() {
		Map<String, DBTableColumn> columnMap = new HashMap<String, DBTableColumn>(columns.size());
		for (DBTableColumn column : columns) {
			columnMap.put(column.getField(), column);
		}
		return columnMap;
	}

	public Map<String, DBIndex> getIndexMap() {
		Map<String, DBIndex> indexMap = new HashMap<String, DBIndex>(indexes.size());
		for (DBIndex index : indexes) {
			indexMap.put(index.getIndexName(), index);
		}
		return indexMap;
	}

	/**
	 * 生成此表的创建SQL语句.
	 * 
	 * @return SQL语句.
	 */
	public String[] getCreateSQL() {
		List<String> sqls = new ArrayList<String>();

		StringBuilder sql = new StringBuilder();
		String primaryKey = null;
		// 创建表
		sql.append("CREATE TABLE " + getNameWithIndex()).append("(");
		for (DBTableColumn column : columns) {
			if (column.isPrimaryKey()) {
				primaryKey = column.getField();
			}
			sql.append(_sqlFieldPhrase(column)).append(",");
		}
		sql.append(" PRIMARY KEY(" + primaryKey + ")");
		sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
		sqls.add(sql.toString());

		// 创建索引
		for (DBIndex dbIndex : indexes) {
			sql.setLength(0);
			sql.append(_sqlCreateIndex(dbIndex));
			sqls.add(sql.toString());
		}
		return sqls.toArray(new String[sqls.size()]);
	}

	/**
	 * 获取修改的sql语句.
	 * 
	 * @param table
	 *            已经存在的表结构
	 * @param isDelete
	 *            多余的列是否删除.
	 * @return
	 */
	@Deprecated
	public String[] getAlterSQL(DBTable table, boolean isDelete) {
		if (!table.getNameWithIndex().equals(getNameWithIndex())) {
			return new String[0];
		}

		List<String> sqls = new ArrayList<String>();
		// 同步字段
		Map<String, DBTableColumn> dbColumnMap = table.getColumnMap();
		DBTableColumn dbColumn = null;
		for (DBTableColumn entityColumn : this.columns) {
			dbColumn = dbColumnMap.get(entityColumn.getField());
			if (dbColumn != null) {
				dbColumnMap.remove(entityColumn.getField());
			}
			if (dbColumn == null) {
				String addSql = "ALTER TABLE " + this.getNameWithIndex() + " ADD COLUMN "
						+ _sqlFieldPhrase(entityColumn) + ";";
				sqls.add(addSql);
			} else if (!entityColumn.equals(dbColumn)) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("db - " + dbColumn);
					LOG.debug("entity - " + entityColumn);
				}
				String modifySql = "ALTER TABLE " + this.getNameWithIndex() + " MODIFY "
						+ _sqlFieldPhrase(entityColumn) + ";";
				sqls.add(modifySql);
			}
		}

		// 同步索引
		Map<String, DBIndex> dbIndexMap = table.getIndexMap();
		DBIndex dbIndex = null;
		for (DBIndex entityIndex : this.indexes) {
			dbIndex = dbIndexMap.get(entityIndex.getIndexName());
			if (dbIndex != null) {
				dbIndexMap.remove(entityIndex.getIndexName());
			}
			if (dbIndex == null) {
				sqls.add(_sqlCreateIndex(entityIndex));
			} else if (!entityIndex.equals(dbIndex)) {
				sqls.add("DROP INDEX " + dbIndex.getIndexName() + " ON " + this.getNameWithIndex());
				sqls.add(_sqlCreateIndex(entityIndex));
			}
		}

		if (isDelete) {
			for (String field : dbColumnMap.keySet()) {
				sqls.add("ALTER TABLE " + this.getNameWithIndex() + " DROP COLUMN " + field + ";");
			}
			for (DBIndex index : dbIndexMap.values()) {
				sqls.add("DROP INDEX " + index.getIndexName() + " ON " + this.getNameWithIndex());
			}
		}

		return sqls.toArray(new String[sqls.size()]);
	}

	/**
	 * 创建索引SQL语句.
	 */
	private String _sqlCreateIndex(DBIndex index) {
		StringBuilder indexSql = new StringBuilder();
		if (index.isUnique()) {
			indexSql.append("CREATE UNIQUE INDEX");
		} else {
			indexSql.append("CREATE INDEX");
		}
		if (StringUtils.isBlank(index.getField())) {
			throw new IllegalArgumentException("索引注解格式错误，field不能为空");
		}
		indexSql.append(" ").append(index.getIndexName()).append(" ON").append(" ").append(this.getNameWithIndex());
		indexSql.append("(").append(index.getField()).append(");");

		return indexSql.toString();
	}

	/**
	 * 生成单列的SQL语句.
	 * 
	 * @param column
	 *            列对象
	 * 
	 * @return 单列的SQL语句.
	 */
	private String _sqlFieldPhrase(DBTableColumn column) {
		StringBuilder pharse = new StringBuilder();

		pharse.append(column.getField()).append(" ");
		switch (DataTypeBind.getEnum(column.getType())) {
		case UPDATETIME:
			pharse.append("timestamp");
			pharse.append(" NOT NULL");
			pharse.append(" DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP");
			break;
		case DATETIME:
			pharse.append("datetime");
			if (!column.isCanNull()) {
				pharse.append(" NOT NULL");
			}
			if (column.isHasDefault())
				pharse.append(" DEFAULT " + column.getDefaultValue());
			break;
		default:
			pharse.append(column.getType());
			if (column.getLength() > 0) {
				pharse.append("(" + column.getLength() + ")");
			}

			if (!column.isCanNull()) {
				pharse.append(" NOT NULL");
			}

			// 全局表的主键一定不能是自增的, 依靠id生成器生成.
			if (column.isAutoIncrement() && shardingNum > 0) {
				pharse.append(" AUTO_INCREMENT");
			}

			if (column.getDefaultValue() != null)
				pharse.append(" DEFAULT " + column.getDefaultValue());
			break;
		}
		pharse.append(" COMMENT '").append(column.getComment()).append("'");

		return pharse.toString();
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public int getTableIndex() {
		return tableIndex;
	}

	public void setTableIndex(int tableIndex) {
		this.tableIndex = tableIndex;
	}

	public int getShardingNum() {
		return shardingNum;
	}

	public void setShardingNum(int shardingNum) {
		this.shardingNum = shardingNum;
	}
}