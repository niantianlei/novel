package util;


public class StringUtils {
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return null == str  || str.equals("")
				|| str.matches("\\s*");
	}

	/**
	 * 给输入的字符串设置默认值
	 * @param content
	 * @param defaultValue
	 * @return
	 */
	public static String defaultValue(String content,String defaultValue){
		if(isEmpty(content)){
			return defaultValue;
		}
		return content;
	}
	
	/**
	 * 把数据库字段名改为驼峰方式
	 * @param column
	 * @return
	 */
	public static String columnToProperty(String column) {
		/**如果字段名为空，就返回空字符串* */
		if(isEmpty(column)) return "";
		/**获取字段的长度，一般来说字段长度不可能有几百个字节的，所以用Byte就行了* */
		Byte length = (byte) column.length();
		
	    StringBuilder sb = new StringBuilder(length);
	    int i = 0;
	    /**遍历字段的每一个字符* */
	    for (int j = 0; j < length; j++) {
	    	 /**匹配到第一个_* */
	        if (column.charAt(j) == '_') {
	            /**如果后面还有_,也就是连续的_,那么j就需要自增一个单位，直到后面不是_为止* */
	            while (column.charAt(j + 1) == '_') {
	            	j += 1;
	            }
	            sb.append(("" + column.charAt(++j)).toUpperCase());
	            
	        } else {
	        	 /**如果循环到的字符不是_,那么就保存下来* */
	                sb.append(column.charAt(j));
	            }
	        }
	 
        return sb.toString();
    }
	    
    /**
	 * 将一个字符串的首字母改成大写
	 * @param str
	 * @return
	 */
	public static String upperCaseFirstCharacter(String str){
		StringBuilder sb = new StringBuilder();
		char[] arr = str.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			if(i==0) sb.append((arr[i] + "").toUpperCase());
			else sb.append((arr[i]+""));
		}
		return sb.toString();
	}
    
}
