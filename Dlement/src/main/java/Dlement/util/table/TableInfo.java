package Dlement.util.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TableInfo {
	//FIXME 支持正则匹配
	//FIXME 支持直接通过文件名实例化
	
	
	
	String tableName;
	List<ColInfo> colInfos = new ArrayList<>();
	Map<String,ColInfo> colInfoMap = new HashMap<String,ColInfo>();
	String colNameRegex;
	List<String> primaryKeyList = new ArrayList<>();
	List<String> indexList = new ArrayList<>();
	
	public TableInfo(){
		
		
	}
	
	public TableInfo(String filePath){
		
		
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	
	public List<ColInfo> getColInfos() {
		return colInfos;
	}
	public Map<String,ColInfo> getColInfoMap() {
		return colInfoMap;
	}
	public void setColInfos(List<ColInfo> colInfos) {
		this.colInfos = colInfos;
	}
	
	public void addCol(ColInfo info){
		if(info.getColName()!=null){
			info.setColName(info.getColName().trim());
		}
		if(info.getColRemark()!=null){
			info.setColRemark(info.getColRemark().trim());
		}
		if(colNameRegex!=null&&!"".equals(colNameRegex.trim())){
			Matcher m = Pattern.compile(colNameRegex).matcher(info.getColName());
			if(m.matches()){
				colInfoMap.put(info.getColName(), info);
				colInfos.add(info);
			}
		}else{
			colInfoMap.put(info.getColName(), info);
			colInfos.add(info);
		}
	}
	
	public ColInfo getCol(String colName){
		ColInfo info = new ColInfo();
		if(colName==null){
			return info;
		}
		for(ColInfo colInfo:colInfos){
			if(colName.equals(colInfo.getColName())){
				info = colInfo;
				break;
			}
		}
		return info;
	}
	
	public void setCol(ColInfo info){
		if(info==null||info.getColName()==null){
			return ;
		}
		for(ColInfo colInfo:colInfos){
			if(info.getColName().equals(colInfo.getColName())){
				//FIXME 后面补充表的类反射
				info.setColRemark(info.getColRemark());
				break;
			}
		}
		
	}

	public String getColNameRegex() {
		return colNameRegex;
	}

	public void setColNameRegex(String colNameRegex) {
		this.colNameRegex = colNameRegex;
	}


	public void setColInfoMap(Map<String, ColInfo> colInfoMap) {
		this.colInfoMap = colInfoMap;
	}

	public List<String> getPrimaryKeyList() {
		return primaryKeyList;
	}

	public void setPrimaryKeyList(List<String> primaryKeyList) {
		this.primaryKeyList = primaryKeyList;
	}

	public List<String> getIndexList() {
		return indexList;
	}

	public void setIndexList(List<String> indexList) {
		this.indexList = indexList;
	}

	public List<String> findSameCols(TableInfo compareTableInfo){
		List<String> result = new ArrayList<>();
		for(ColInfo colInfo : this.colInfos){
			if(compareTableInfo.getColInfoMap().get(colInfo.getColName())!=null){
				result.add(colInfo.getColName());
			}
		}
		return result;
	}
	public boolean isSameCols(TableInfo compareTableInfo){
		boolean isSame = true;
		for(ColInfo colInfo : this.colInfos){
			if(compareTableInfo.getColInfoMap().get(colInfo.getColName())!=null){
				isSame =false;
			}
		}
		return isSame;
	}
}
