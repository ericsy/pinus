package com.pinus.generator;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import com.pinus.cluster.beans.DBTable;
import com.pinus.exception.DDLException;

/**
 * 数据库表生成器接口.
 * 
 * @author duanbn
 */
public interface IDBGenerator {

    /**
     * 扫描@Table类
     * 
     * @param scanPackage
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public List<DBTable> scanEntity(String scanPackage) throws IOException, ClassNotFoundException;

    /**
     * 获取数据库中的表.
     *
     * @param conn 数据库连接
     *
     * @return 表名
     */
    public List<String> getTable(Connection conn) throws DDLException;

    /**
     * 同步数据库表
     * 
     * @param table
     *
     * @throws DDLException
     */
    public void syncTable(Connection conn, DBTable table) throws DDLException;

    /**
     * 批量同步数据库表.
     * 表下标从0开始.
     *
     * @param conn 数据库连接.
     * @param table 库表
     * @param num 需要生成的分表数
     */
    public void syncTable(Connection conn, DBTable table, int num) throws DDLException;

}