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

package org.pinus.datalayer.beans;

/**
 * 集群遍历器信息.
 * 
 * @author duanbn
 * @since 0.6.0
 */
public class DBClusterIteratorInfo {

	private int latestDbIndex;

	private int latestTableIndex;

	private long latestId;

	public DBClusterIteratorInfo(int latestDbIndex, int latestTableIndex, long latestId) {
		this.latestDbIndex = latestDbIndex;
		this.latestTableIndex = latestTableIndex;
		this.latestId = latestId;
	}

	@Override
	public String toString() {
		return "DBClusterIteratorInfo [latestDbIndex=" + latestDbIndex + ", latestTableIndex=" + latestTableIndex
				+ ", latestId=" + latestId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + latestDbIndex;
		result = prime * result + (int) (latestId ^ (latestId >>> 32));
		result = prime * result + latestTableIndex;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DBClusterIteratorInfo other = (DBClusterIteratorInfo) obj;
		if (latestDbIndex != other.latestDbIndex)
			return false;
		if (latestId != other.latestId)
			return false;
		if (latestTableIndex != other.latestTableIndex)
			return false;
		return true;
	}

	public int getLatestDbIndex() {
		return latestDbIndex;
	}

	public void setLatestDbIndex(int latestDbIndex) {
		this.latestDbIndex = latestDbIndex;
	}

	public int getLatestTableIndex() {
		return latestTableIndex;
	}

	public void setLatestTableIndex(int latestTableIndex) {
		this.latestTableIndex = latestTableIndex;
	}

	public long getLatestId() {
		return latestId;
	}

	public void setLatestId(int latestId) {
		this.latestId = latestId;
	}

}