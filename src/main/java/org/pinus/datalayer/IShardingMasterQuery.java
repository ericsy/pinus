/**
 * Copyright 2014 Duan Bingnan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.pinus.datalayer;

import java.util.List;
import java.util.Map;

import org.pinus.api.IShardingKey;
import org.pinus.api.SQL;
import org.pinus.api.query.IQuery;
import org.pinus.cache.IPrimaryCache;
import org.pinus.cache.ISecondCache;
import org.pinus.cluster.IDBCluster;
import org.pinus.exception.DBOperationException;

/**
 * 主库查询接口.
 * 
 * @author Asia
 */
public interface IShardingMasterQuery {

	/**
	 * 查询全局库表的数量.
	 * 
	 * @param clusterName
	 *            集群名
	 * @param clazz
	 *            表示全局表的实体对象
	 * @return count数
	 */
	public Number getGlobalCountFromMaster(String clusterName, Class<?> clazz, boolean useCache);

	/**
	 * 
	 * @param query
	 * @param clusterName
	 * @param clazz
	 * @return
	 */
	public Number getGlobalCountFromMaster(IQuery query, String clusterName, Class<?> clazz);

	/**
	 * 根据pk查询全局表中的数据. 查询不到则返回null
	 * 
	 * @param pk
	 *            主键
	 * @param clusterName
	 *            集群名
	 * @param clazz
	 *            实体对象
	 * @return 数据
	 */
	public <T> T findGlobalByPkFromMaster(Number pk, String clusterName, Class<T> clazz);

	public <T> T findGlobalByPkFromMaster(Number pk, String clusterName, Class<T> clazz, boolean useCache);

	/**
	 * 根据Query对象查询全局表数据. 查询不到则返回null
	 * 
	 * @param query
	 *            Query条件
	 * @param clusterName
	 *            集群名
	 * @param clazz
	 *            实体对象
	 * @return 数据
	 */
	public <T> T findGlobalOneByQueryFromMaster(IQuery query, String clusterName, Class<T> clazz, boolean useCache);

	/**
	 * 根据主键查询全局表数据. 当查询不到数据时返回空的List，不会返回null.
	 * 
	 * @param clusterName
	 *            集群名
	 * @param clazz
	 *            实体对象
	 * @param pks
	 *            主键
	 * @return 数据
	 */
	public <T> List<T> findGlobalByPksFromMaster(String clusterName, Class<T> clazz, Number... pks);
	
	public <T> List<T> findGlobalByPksFromMaster(String clusterName, Class<T> clazz, boolean useCache, Number... pks);

	/**
	 * 根据主键查询全局表数据. 当查询不到数据时返回空的List，不会返回null.
	 * 
	 * @param clusterName
	 *            集群名
	 * @param clazz
	 *            实体对象
	 * @param pks
	 *            主键
	 * @return 数据
	 */
	public <T> List<T> findGlobalByPkListFromMaster(List<? extends Number> pks, String clusterName, Class<T> clazz,
			boolean useCache);

	/**
	 * 根据sql查询全局表. 当查询不到数据时返回空的List，不会返回null.
	 * 
	 * @param sql
	 *            查询语句
	 * @param clusterName
	 *            集群名
	 * @return 数据
	 */
	public <T> List<T> findGlobalBySqlFromMaster(SQL sql, String clusterName);

	/**
	 * 根据Query查询全局表. 当查询不到数据时返回空的List，不会返回null.
	 * 
	 * @param query
	 *            Query对象
	 * @param clusterName
	 *            集群名
	 * @param clazz
	 *            实体对象
	 * @return 数据
	 */
	public <T> List<T> findGlobalByQueryFromMaster(IQuery query, String clusterName, Class<T> clazz, boolean useCache);

	/**
	 * 设置数据库集群.
	 */
	public void setDBCluster(IDBCluster dbCluster);

	/**
	 * 获取集群总数.
	 * 
	 * @param clazz
	 * @return
	 */
	public Number getCountFromMaster(Class<?> clazz, boolean useCache);

	/**
	 * 根据查询条件获取集群记录数.
	 * 
	 * @param clazz
	 *            实体对象
	 * @param query
	 *            查询条件
	 * 
	 * @return 集群记录数
	 */
	public Number getCountFromMaster(Class<?> clazz, IQuery query);

	/**
	 * 获取分库分表记录总数.
	 * 
	 * @param shardingValue
	 *            分库分表因子
	 * @param clazz
	 *            数据对象
	 * 
	 * @return 表记录总数
	 * @throws DBOperationException
	 *             操作失败
	 * @throws IllegalArgumentException
	 *             输入参数错误
	 */
	public Number getCountFromMaster(IShardingKey<?> shardingValue, Class<?> clazz, boolean useCache);

	/**
	 * 根据查询条件获取某一个分片的记录数.
	 */
	public Number getCountFromMaster(IQuery query, IShardingKey<?> shardingValue, Class<?> clazz);

	/**
	 * 一个主分库分表, 根据主键查询. 查询不到则返回null
	 * 
	 * @param pk
	 *            主键
	 * @param shardingValue
	 *            分库分表因子
	 * @param clazz
	 *            数据对象类型
	 * 
	 * @return 查询结果，找不到返回null
	 * 
	 * @throws DBOperationException
	 *             操作失败
	 * @throws IllegalArgumentException
	 *             输入参数错误
	 */
	public <T> T findByPkFromMaster(Number pk, IShardingKey<?> shardingValue, Class<T> clazz, boolean useCache);

	/**
	 * 根据查询条件获取一条数据. 如果查询到多条则返回第一条.当查询不到数据时返回空的List，不会返回null.
	 * 
	 * @param query
	 * @param shardingValue
	 * @param clazz
	 * @return 查询结果，找不到返回null
	 */
	public <T> T findOneByQueryFromMaster(IQuery query, IShardingKey<?> shardingValue, Class<T> clazz, boolean useCache);

	/**
	 * 一个主分库分表, 根据多个主键查询.当查询不到数据时返回空的List，不会返回null.
	 * 
	 * @param shardingValue
	 *            分库分表因子
	 * @param clazz
	 *            数据对象类型
	 * @param pks
	 *            主键
	 * 
	 * @return 查询结果
	 * 
	 * @throws DBOperationException
	 *             操作失败
	 * @throws IllegalArgumentException
	 *             输入参数错误
	 */
	public <T> List<T> findByPksFromMaster(IShardingKey<?> shardingValue, Class<T> clazz, Number... pks);
	
	public <T> List<T> findByPksFromMaster(IShardingKey<?> shardingKey, Class<T> clazz, boolean useCache, Number... pks);

	/**
	 * 一个主分库分表, 根据多个主键查询.当查询不到数据时返回空的List，不会返回null.
	 * 
	 * @param pks
	 *            主键数组
	 * @param shardingValue
	 *            分库分表因子
	 * @param clazz
	 *            数据对象类型
	 * 
	 * @return 查询结果
	 * 
	 * @throws DBOperationException
	 *             操作失败
	 * @throws IllegalArgumentException
	 *             输入参数错误
	 */
	public <T> List<T> findByPkListFromMaster(List<? extends Number> pks, IShardingKey<?> shardingValue,
			Class<T> clazz, boolean useCache);

	/**
	 * 多个主分库分表, 多个主键查询, 一个主键对应一个分库分表.
	 * <b>主键列表和分库分表因子的列表必须是一一对应，每一个分库分表只能查出一条记录</b> 当查询不到数据时返回空的List，不会返回null.
	 * 
	 * @param shardingValues
	 *            分库分表因子列表
	 * @param clazz
	 *            数据对象类型
	 * @param pks
	 *            主键数组
	 * 
	 * @return 查询结果
	 * 
	 * @throws DBOperationException
	 *             操作失败
	 * @throws IllegalArgumentException
	 *             输入参数错误
	 */
	@Deprecated
	public <T> List<T> findByShardingPairFromMaster(List<IShardingKey<?>> shardingValues, Class<T> clazz, Number... pks);

	/**
	 * 多个主分库分表, 多个主键查询. 主键列表和分库分表因子的列表必须是一一对应, 当查询不到数据时返回空的List，不会返回null.
	 * 
	 * @param pks
	 *            主键数组
	 * @param shardingValues
	 *            分库分表因子列表
	 * @param clazz
	 *            数据对象类型
	 * 
	 * @return 查询结果
	 * 
	 * @throws DBOperationException
	 *             操作失败
	 * @throws IllegalArgumentException
	 *             输入参数错误
	 */
	public <T> List<T> findByShardingPairFromMaster(List<? extends Number> pks, List<IShardingKey<?>> shardingValues,
			Class<T> clazz, boolean useCache);

	/**
	 * 一个主分库分表, 根据条件查询.当查询不到数据时返回空的List，不会返回null.
	 * 
	 * @param sql
	 *            查询语句
	 * @param shardingValue
	 *            分库分表因子
	 * 
	 * @return 查询结果
	 * 
	 * @throws DBOperationException
	 *             操作失败
	 * @throws IllegalArgumentException
	 *             输入参数错误
	 */
	public List<Map<String, Object>> findBySqlFromMaster(SQL sql, IShardingKey<?> shardingValue);

	/**
	 * 根据查询条件对象进行查询.当查询不到数据时返回空的List，不会返回null.
	 * 
	 * @param query
	 *            查询条件
	 * @param shardingValue
	 *            分库分表因子
	 * 
	 * @throws DBOperationException
	 *             操作失败
	 * @throws IllegalArgumentException
	 *             输入参数错误
	 */
	public <T> List<T> findByQueryFromMaster(IQuery query, IShardingKey<?> shardingValue, Class<T> clazz,
			boolean useCache);

	/**
	 * 设置缓存. 当不设置时则不适用缓存
	 */
	public void setPrimaryCache(IPrimaryCache primaryCache);

	/**
	 * 设置二级缓存.
	 * 
	 * @param secondCache
	 */
	public void setSecondCache(ISecondCache secondCache);

}
