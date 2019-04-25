package Dlement.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Dlement.util.IOUtil;

public class test implements Cloneable{

	public String s;
	public String gets(){
		return s;
	}
	
	public static final String DEFAULT_FILE_PATH = "D:/javaio/";
	public static final String DEFAULT_FILE_TABLE_PATH = "D:/javaio/table/";
	
	public static void main(String[] args)  {
		
		List<String> list = IOUtil.readFileList(DEFAULT_FILE_PATH+"test.txt");
//		List<String> list2 = IOUtil.readFileList(DEFAULT_FILE_PATH+"test2.txt");
		int i=0;
		for(String l : list) {
			
			System.out.println("INSERT INTO STAT_ORG(per_org,org_code) values('"+l+"','"+list.get(i)+"');");
			i++;
		}
//		String sql = "";
//		sql+=" left join(";
//		sql+=" select"; 
//		sql+=" t.user_id,";
//		sql+=" case ";
//		sql+=" when t.education in ('11','12','13','1A','1B') then 'YJS'";
//		sql+=" when t.education in ('21','23','24','2A','2B','2C','2D') then 'DXBK'";
//		sql+=" when t.education in ('22') then 'DXZK'";
//		sql+=" when t.education in ('41') then 'ZDZK'";
//		sql+=" when t.education in ('44','47','6') then 'GZZJ'";  
//		sql+=" when t.education in ('7','8','9') then 'CZ'"; 
//		sql+=" ELSE 'NULL' END AS EDU";
//		sql+=" FROM PUB_USER t";
//		sql+=" )t3 on t3.user_id = t.user_id ";
//		System.out.println(sql);
//		String path = DEFAULT_FILE_TABLE_PATH+"party";
//		List<String> fileNames = IOUtil.readSubFileNames(path);
//		List<String> filterFileNames = new ArrayList<>();
//		for(String fileName: fileNames) {
//			if(fileName.matches("(.+?)\\.tab")) {
//				filterFileNames.add(fileName);
//			}
//		}
//		String outPutFile = DEFAULT_FILE_TABLE_PATH+"output/";
//		StringBuilder sb = new StringBuilder();
//		for(String fileName : filterFileNames) {
//			if(fileName.startsWith("bak")) {
//				continue;
//			}
//			String result = IOUtil.readFile(path+"/"+fileName);
//			sb.append(result+"\n");
//			
//		}
//		IOUtil.outputFile(outPutFile+ "create_table_party.sql", sb.toString());
	}
	
	public static void changeCode(String typeId,String tableName) {
		
		List<String> list =IOUtil.readFileList(DEFAULT_FILE_PATH+"test.txt");
		Set<String> set = new HashSet<>();
		List<String> resultDelete = new ArrayList<>();
		List<String> resultInsert = new ArrayList<>();
		List<String> resultUpdate = new ArrayList<>();
		LinkedHashMap<String, String> lMap = new LinkedHashMap<>();
		
		for(String l : list ) {
			String[] arr = l.split("\t");
			if("".equals(l)) {
				continue;
			}
			
			String fromCode = arr[0].trim();
			String toCode = arr[1].trim();
			String codeName = arr[2].trim();
			String order = arr[3].trim();
			String forbid = arr[4].trim();
//			String delete = arr[5].trim();
			
			boolean isInsert = "".equals(fromCode);
			boolean isForbid = "1".equals(forbid);
			boolean isDelete = "".equals(toCode);
			
			String sql = "UPDATE "+tableName+" t SET t.org_category_new= '"+toCode+"' WHERE t.org_category_new = '"+fromCode+"' AND t.org_Code LIKE 'C59E19%';";
			if(!"".equals(fromCode)&&!fromCode.equals(toCode)) {
				System.out.println(sql);
			}
			
		
			String codeSql=  "";
				
			if(set.contains(toCode)) {
				codeSql = "DELETE FROM PUB_CODE T WHERE T.CODE_VALUE='"+fromCode+"' AND T.TYPE_ID = '"+typeId+"';";
				resultDelete.add(codeSql);
				continue;
			}
			
			if(isInsert) {
				codeSql = "INSERT INTO PUB_CODE(TYPE_ID,CODE_VALUE,CODE_NAME,CODE_ORDER,FLAG) VALUES('"+typeId+"','"+toCode+"','"+codeName+"','"+order+"','"+(isForbid?"1":"0")+"');";
				resultInsert.add(codeSql);
			}else if(isDelete){
				codeSql = "DELETE FROM PUB_CODE t WHERE t.TYPE_ID ='"+typeId+"' AND t.CODE_VALUE='"+fromCode+"';";
				resultDelete.add(codeSql);
			}else {
				codeSql = "UPDATE PUB_CODE t SET t.code_value='"+toCode+"' ";
				if(!"".equals(codeName)) {
					codeSql += ",t.code_name='"+codeName+"'";
				}
				codeSql += " WHERE t.type_id = '"+typeId+"' AND t.code_value='"+fromCode+"';";
				resultUpdate.add(codeSql);
				lMap.put(fromCode, toCode);
				set.add(toCode);
			}
			
		}
		printList(resultDelete);
		printList(resultUpdate);
		printList(resultInsert);
	}
	
	public static boolean checkMapOrder(LinkedHashMap<String,String> checkMap) {
		for(Entry<String,String> entry : checkMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			boolean isCompare = false;
			for(Entry<String,String> subEntry : checkMap.entrySet()) {
				if(key.equals(subEntry.getKey())) {
					isCompare = true;
					continue;
				}
				if(isCompare&&subEntry.getKey().equals(key)) {
					return false;
				}
				
			}
		}
		
		return true;
	}
	
	public static String getParamStr(String[] sr){
		StringBuilder sb = new StringBuilder();
		for(String s:sr){
			sb.append(",'"+s.trim()+"'");
		}
		
		return sb.toString().replaceFirst(",", "");
	}
	
	

	
	public static String getFormatAppend(){
		StringBuilder sb = new StringBuilder();
		String filePath = "d:/javaio/";
		String fileName = "line.txt";
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath+fileName),"gb2312"));
			String line = "";
			int i =0;
			while((line=br.readLine())!=null){
				line = line.replaceFirst("(\\s+){1}", "");
				sb.append("contentHtml.append(\""+line+"\");"+"\n");
				i++;
			}
//			System.out.println("鎬诲叡"+i);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	
	
	
	public static String getFormatGrid(){
		StringBuilder sb = new StringBuilder();
		String filePath = "d:/javaio/";
		String fileName = "line.txt";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath+fileName),"gb2312"));
			String line = "";
			while((line=br.readLine())!=null){
				if("offset++;".equals(line.trim())||"".equals(line.trim())){
					continue;
				}
				line = line.replaceFirst("(\\s+){1}", "");
				line = line.replaceFirst("\\);$", "");
				line = line.replaceFirst("form.add\\(((\\s+){0,1}offset,(\\s+){0,1}\\d,(\\s+){0,1})+", "");
				String eleName = getElementName(line);
				sb.append("form.add(\""+eleName+"\",1);\n");
				sb.append("form.add("+line+",3);\n");
//				form.add("35宀佷互涓嬪湪宀楄亴宸ユ暟",1);
//				form.add(new Text("d1692", "35宀佷互涓嬪湪宀楄亴宸ユ暟", companyInfo.getD1692()==null?"":companyInfo.getD1692().toString()),3);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	public static String getElementName(String line){
		Matcher m = Pattern.compile("\"(.{0,}?)\"").matcher(line);
		int findIndex = 0;
		while(m.find()){
			if(++findIndex==2){
				return m.group(1);
			}
		}
		return "";
	}
	
	public static String toMap(){
			StringBuilder sb = new StringBuilder();
			String filePath = "d:/javaio/";
			String fileName = "line.txt";
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath+fileName),"gb2312"));
				String line = "";
				while((line=br.readLine())!=null){
					if("".equals(line.trim())){
						continue;
					}
					if(line.startsWith("\\s")){
						line = line.replaceFirst("(\\s+){1}", "");
					}
					String[] s = line.split("\\s+");
					if(s.length>1){
						sb.append("map.put(\""+s[0]+"\",\""+s[1]+"\");\n");
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return sb.toString();
	}
	
	public static String addPlaceHolder(){
		StringBuilder sb = new StringBuilder();
		String filePath = "d:/javaio/";
		String fileName = "line.txt";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath+fileName),"gb2312"));
			String line = "";
			while((line=br.readLine())!=null){
				if("".equals(line.trim())){
					continue;
				}
				
				if(line.startsWith("\\s")||line.startsWith("\r")){
					line = line.replaceFirst("(\\s+){1}", "");
				}
				line = line.replaceFirst("(\\s+){1}", "");
				if(line.length()<50){
					sb.append(line+"\n");
					continue;
				}
				String eleName = getElementName(line);
				sb.append(line.substring(0, line.lastIndexOf(","))+".setPlaceholder(\""+eleName+"\")"+line.substring(line.lastIndexOf(","))+"\n");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public static String add(){
		StringBuilder sb = new StringBuilder();
		String filePath = "d:/javaio/";
		String fileName = "line.txt";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath+fileName),"gb2312"));
			String line = "";
			while((line=br.readLine())!=null){
				line = line.replaceFirst("(\\s+){1}", "");
				String[] args =  line.split("\\s");
				if(args.length!=0){
					sb.append("cols.add(\""+args[0]+"\");\n");
				}
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	public static String getParamNon(int no){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<no;i++){
			sb.append(",?");
		}
		return sb.toString().replaceFirst(",", "");
	}
	
	public String getInsertSql(String tableName,String colName){
//		
//		alter table D16 add d16b22 varchar2(20);
//		comment on column D16.d16b21 is '涓荤閮ㄩ棬';
		String sql = "";
		return sql;
	}
	public String getInsertSql(String tableName,String colName,String callbackTable){
		String sql = "";
		return sql;
	}
	
	public static List<String> getXML(String xmlPath){
		File f = new File(xmlPath);
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(f);
			
			
//			TransformerFactory  tf  =  TransformerFactory.newInstance();
//			Transformer t = tf.newTransformer();
//			t.setOutputProperty("encoding","GB23121");//瑙ｅ喅涓枃闂锛岃瘯杩囩敤GBK涓嶈
//			ByteArrayOutputStream  bos  =  new  ByteArrayOutputStream();
//			t.transform(new DOMSource(doc), new StreamResult(bos));
//			String xmlStr = bos.toString();
//			
//			System.out.println(xmlStr);
			
			
			
			
			
			NodeList list = doc.getElementsByTagName("Table");
			
			for(int i=0;i<list.getLength();i++){
				Node node = list.item(i);
				System.out.println(node);
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//		catch (TransformerConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (TransformerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static List<Class> getAllClassByInterface(Class c) {
        List<Class>  returnClassList = null;
        
        if(c.isInterface()) {
            // 鑾峰彇褰撳墠鐨勫寘鍚�
            String packageName = c.getPackage().getName();
            // 鑾峰彇褰撳墠鍖呬笅浠ュ強瀛愬寘涓嬫墍浠ョ殑绫�
            List<Class<?>> allClass = getClasses(packageName);
            if(allClass != null) {
                returnClassList = new ArrayList<Class>();
                for(Class classes : allClass) {
                    // 鍒ゆ柇鏄惁鏄悓涓�涓帴鍙�
                    if(c.isAssignableFrom(classes)) {
                        // 鏈韩涓嶅姞鍏ヨ繘鍘�
                        if(!c.equals(classes)) {
                            returnClassList.add(classes);        
                        }
                    }
                }
            }
        }
        
        return returnClassList;
    }
	
	public static List<Class<?>> getClasses(String packageName){
        
        //绗竴涓猚lass绫荤殑闆嗗悎
        List<Class<?>> classes = new ArrayList<Class<?>>();
        //鏄惁寰幆杩唬
        boolean recursive = true;
        //鑾峰彇鍖呯殑鍚嶅瓧 骞惰繘琛屾浛鎹�
        String packageDirName = packageName.replace('.', '/');
        //瀹氫箟涓�涓灇涓剧殑闆嗗悎 骞惰繘琛屽惊鐜潵澶勭悊杩欎釜鐩綍涓嬬殑things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            //寰幆杩唬涓嬪幓
            while (dirs.hasMoreElements()){
                //鑾峰彇涓嬩竴涓厓绱�
                URL url = dirs.nextElement();
                //寰楀埌鍗忚鐨勫悕绉�
                String protocol = url.getProtocol();
                //濡傛灉鏄互鏂囦欢鐨勫舰寮忎繚瀛樺湪鏈嶅姟鍣ㄤ笂
                if ("file".equals(protocol)) {
                    //鑾峰彇鍖呯殑鐗╃悊璺緞
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    //浠ユ枃浠剁殑鏂瑰紡鎵弿鏁翠釜鍖呬笅鐨勬枃浠� 骞舵坊鍔犲埌闆嗗悎涓�
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)){
                    //濡傛灉鏄痡ar鍖呮枃浠� 
                    //瀹氫箟涓�涓狫arFile
                    JarFile jar;
                    try {
                        //鑾峰彇jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        //浠庢jar鍖� 寰楀埌涓�涓灇涓剧被
                        Enumeration<JarEntry> entries = jar.entries();
                        //鍚屾牱鐨勮繘琛屽惊鐜凯浠�
                        while (entries.hasMoreElements()) {
                            //鑾峰彇jar閲岀殑涓�涓疄浣� 鍙互鏄洰褰� 鍜屼竴浜沯ar鍖呴噷鐨勫叾浠栨枃浠� 濡侻ETA-INF绛夋枃浠�
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            //濡傛灉鏄互/寮�澶寸殑
                            if (name.charAt(0) == '/') {
                                //鑾峰彇鍚庨潰鐨勫瓧绗︿覆
                                name = name.substring(1);
                            }
                            //濡傛灉鍓嶅崐閮ㄥ垎鍜屽畾涔夌殑鍖呭悕鐩稿悓
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                //濡傛灉浠�"/"缁撳熬 鏄竴涓寘
                                if (idx != -1) {
                                    //鑾峰彇鍖呭悕 鎶�"/"鏇挎崲鎴�"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                //濡傛灉鍙互杩唬涓嬪幓 骞朵笖鏄竴涓寘
                                if ((idx != -1) || recursive){
                                    //濡傛灉鏄竴涓�.class鏂囦欢 鑰屼笖涓嶆槸鐩綍
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        //鍘绘帀鍚庨潰鐨�".class" 鑾峰彇鐪熸鐨勭被鍚�
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            //娣诲姞鍒癱lasses
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                      }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       
        return classes;
    }
	
	
	/**
	 * 浠ユ枃浠剁殑褰㈠紡鏉ヨ幏鍙栧寘涓嬬殑鎵�鏈塁lass
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<Class<?>> classes){
	    //鑾峰彇姝ゅ寘鐨勭洰褰� 寤虹珛涓�涓狥ile
	    File dir = new File(packagePath);
	    //濡傛灉涓嶅瓨鍦ㄦ垨鑰� 涔熶笉鏄洰褰曞氨鐩存帴杩斿洖
	    if (!dir.exists() || !dir.isDirectory()) {
	        return;
	    }
	    //濡傛灉瀛樺湪 灏辫幏鍙栧寘涓嬬殑鎵�鏈夋枃浠� 鍖呮嫭鐩綍
	    File[] dirfiles = dir.listFiles(new FileFilter() {
	    	//鑷畾涔夎繃婊よ鍒� 濡傛灉鍙互寰幆(鍖呭惈瀛愮洰褰�) 鎴栧垯鏄互.class缁撳熬鐨勬枃浠�(缂栬瘧濂界殑java绫绘枃浠�)
	        public boolean accept(File file) {
	           return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
	        }
	    });
	    //寰幆鎵�鏈夋枃浠�
	    for (File file : dirfiles) {
	        //濡傛灉鏄洰褰� 鍒欑户缁壂鎻�
	        if (file.isDirectory()) {
	            findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
	        }else {
	            //濡傛灉鏄痡ava绫绘枃浠� 鍘绘帀鍚庨潰鐨�.class 鍙暀涓嬬被鍚�
	            String className = file.getName().substring(0, file.getName().length() - 6);
	            try {
	                //娣诲姞鍒伴泦鍚堜腑鍘�
	                classes.add(Class.forName(packageName + '.' + className));
	            } catch (ClassNotFoundException e) {
	                e.printStackTrace();
	            }
	       }
	    }
	}
	public static int printfLength(String s){
		System.out.println(s.split(",").length);
		return s.split(",").length;
	}
	
	public static void copyFile(String fromPath,String toPath){
		
		try {
			OutputStream os = new FileOutputStream(new File(toPath));
			InputStream is = new FileInputStream(new File(fromPath));
			byte[] buffer = new byte[1024];
			int flag = 0;
			while( (flag = is.read(buffer))!=-1 ){
				os.write(buffer,0,flag);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	//FIXME 姹傝川鏁�
//	String regex = "^1?$|^(11+?)\\1+$";
//	StringBuffer sb = new StringBuffer();
//	for (int i = 1; i <= 5000; i++) {
//		sb.append("1");
//		if (!sb.toString().matches(regex)) {
//			System.out.println(i);
//		}
//	}
	
	public static void printfFormatSql(String fileName,String columnName){
		List<String> userIds = IOUtil.readFileList(fileName);
		StringBuilder sb = new StringBuilder();
		int splitLength = 500;
		for(int i=splitLength;i<userIds.size();i+=splitLength){
			StringBuilder subsb = new StringBuilder();
			subsb.append(" OR "+columnName+" IN(");
			for(int j=i-splitLength;j<i;j++){
				
				subsb.append(",'");
				subsb.append(userIds.get(j));
				subsb.append("'");
				
			}
			subsb.append(") ");
			sb.append(subsb.toString().replaceFirst(",", ""));
		}
		
		IOUtil.outputFile("D:\\javaio\\result.txt", sb.toString().replaceFirst("OR", ""));
		
	}
	

	  /**
		 * 璁＄畻涓や釜鏃堕棿涔嬮棿闂撮殧鍑犲ぉ
		 * @param startDate
		 * @param endDate
		 * @return
		 * @throws ParseException
		 */
	    public static int daysBetween(Date startDate,Date endDate){    
	    	int daysBetween = -1;
	    	if(startDate==null||endDate==null){
	    		return daysBetween;
	    	}
	    	try{
		    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				startDate=sdf.parse(sdf.format(startDate));
				endDate=sdf.parse(sdf.format(endDate));
				Calendar cal = Calendar.getInstance();
				cal.setTime(startDate);
				long time1 = cal.getTimeInMillis();
				cal.setTime(endDate);
				long time2 = cal.getTimeInMillis();
				long betweenDays=(time2-time1)/(1000*3600*24);
				daysBetween = Integer.parseInt(String.valueOf(betweenDays));
	    	}catch (ParseException e) {
				// TODO Auto-generated catch block
			}
	    	
	    	return daysBetween;
	    }    
	public static void printList(List<String> list) {
		for(String l : list) {
			System.out.println(l);
		}
	}
}
