package Dlement.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils
{
  private static final String ENCODING = "UTF-8";
  
  public static boolean notEmpty(String str)
  {
    return (str != null) && (str.length() > 0);
  }
  
  public static boolean isEmpty(String str)
  {
    return (str == null) || (str.length() == 0);
  }
  
  public static boolean notEmpty(Collection<?> c)
  {
    return (c != null) && (c.size() > 0);
  }
  
  public static boolean isEmpty(Collection<?> c)
  {
    return (c == null) || (c.size() <= 0);
  }
  
  public static boolean notEmpty(Object[] os)
  {
    return (os != null) && (os.length > 0);
  }
  
  public static boolean isEmpty(Object[] os)
  {
    return (os == null) || (os.length <= 0);
  }
  
  public static boolean notEmpty(Map<?, ?> m)
  {
    return (m != null) && (!m.isEmpty());
  }
  
  public static boolean isEmpty(Map<?, ?> m)
  {
    return (m == null) || (m.isEmpty());
  }
  
  public static void escapeXMLword(StringBuilder appendTo, String src)
  {
    if (src == null) {
      return;
    }
    char[] ca = src.toCharArray();
    for (int i = 0; i < ca.length; i++) {
      switch (ca[i])
      {
      case '&': 
        appendTo.append("&amp;");
        break;
      case '<': 
        appendTo.append("&lt;");
        break;
      case '>': 
        appendTo.append("&gt;");
        break;
      case '"': 
        appendTo.append("&quot;");
        break;
      case '\'': 
        appendTo.append("&#39;");
        break;
      case '\r': 
        appendTo.append('\r');
        break;
      case '\n': 
        appendTo.append('\n');
        break;
      default: 
        if ((ca[i] >= ' ') && ((ca[i] <= '') || (ca[i] >= 'Ā'))) {
          appendTo.append(ca[i]);
        }
        break;
      }
    }
  }
  
  public static String escapeXMLword(String src)
  {
    StringBuilder sb = new StringBuilder();
    escapeXMLword(sb, src);
    return sb.toString();
  }
  
  public static void copyPropertiesExact(Object to, Object from)
  {
    if ((from == null) || (to == null)) {
      return;
    }
    CopyPropertiesTemplate tmpl = getCopyPropertiesTemplate(to.getClass(), from.getClass());
    tmpl.copyPropertiesExact(to, from);
  }
  
  private static class CopyPropertiesTemplate
  {
    private List<Method> fromMethods;
    private List<Method> toMethods;
    
    public CopyPropertiesTemplate(Class<?> clzto, Class<?> clzfrom)
    {
      this.fromMethods = new ArrayList();
      this.toMethods = new ArrayList();
      Method[] fromM = clzfrom.getMethods();
      for (int i = 0; i < fromM.length; i++)
      {
        String methodName = fromM[i].getName();
        Class<?> returnType = fromM[i].getReturnType();
        Class[] parapType = fromM[i].getParameterTypes();
        if ((Utils.CARE_TYPES.contains(returnType)) && (
          (parapType == null) || (parapType.length == 0)))
        {
          String setterName = null;
          if (methodName.startsWith("get"))
          {
            setterName = "set" + methodName.substring(3);
          }
          else
          {
            if (!methodName.startsWith("is")) {
              continue;
            }
            setterName = "set" + methodName.substring(2);
          }
          try
          {
            Method setter = clzto.getMethod(setterName, 
              new Class[] { returnType });
            if (setter != null)
            {
              this.fromMethods.add(fromM[i]);
              this.toMethods.add(setter);
            }
          }
          catch (Exception localException) {}
        }
      }
    }
    
    public void copyPropertiesExact(Object to, Object from)
    {
      for (int i = 0; i < this.fromMethods.size(); i++) {
        try
        {
          ((Method)this.toMethods.get(i)).invoke(to, new Object[] { ((Method)this.fromMethods.get(i)).invoke(from, 
            Utils.EMPTY_PARAM) });
        }
        catch (Exception localException) {}
      }
    }
  }
  
  private static HashMap<CopyPropertiesTemplateKey, CopyPropertiesTemplate> TEMPLATES = new HashMap();
  
  private static CopyPropertiesTemplate getCopyPropertiesTemplate(Class<?> clz1, Class<?> clz2)
  {
    CopyPropertiesTemplateKey key = new CopyPropertiesTemplateKey(clz1, clz2);
    CopyPropertiesTemplate tmpl = (CopyPropertiesTemplate)TEMPLATES.get(key);
    if (tmpl == null)
    {
      tmpl = new CopyPropertiesTemplate(clz1, clz2);
      TEMPLATES.put(key, tmpl);
    }
    return tmpl;
  }
  
  private static class CopyPropertiesTemplateKey
  {
    private Class<?> clz1;
    private Class<?> clz2;
    
    public CopyPropertiesTemplateKey(Class<?> clz1, Class<?> clz2)
    {
      if ((clz1 == null) || (clz2 == null)) {
        throw new NullPointerException("can not copy properties for null.");
      }
      this.clz1 = clz1;
      this.clz2 = clz2;
    }
    
    public boolean equals(Object key)
    {
      if (key == null) {
        return false;
      }
      if (key == this) {
        return true;
      }
      if (!(key instanceof CopyPropertiesTemplateKey)) {
        return false;
      }
      CopyPropertiesTemplateKey cast = (CopyPropertiesTemplateKey)key;
      return (cast.clz1 == this.clz1) && (cast.clz2 == this.clz2);
    }
    
    public int hashCode()
    {
      return this.clz1.hashCode() ^ this.clz2.hashCode();
    }
  }
  
  private static final Object[] EMPTY_PARAM = new Object[0];
  private static final Set<Class<?>> CARE_TYPES = new HashSet();
  
  static
  {
    CARE_TYPES.add(Byte.TYPE);
    CARE_TYPES.add(Byte.class);
    CARE_TYPES.add(Short.TYPE);
    CARE_TYPES.add(Short.class);
    CARE_TYPES.add(Integer.TYPE);
    CARE_TYPES.add(Integer.class);
    CARE_TYPES.add(Long.TYPE);
    CARE_TYPES.add(Long.class);
    CARE_TYPES.add(Float.TYPE);
    CARE_TYPES.add(Float.class);
    CARE_TYPES.add(Double.TYPE);
    CARE_TYPES.add(Double.class);
    CARE_TYPES.add(BigInteger.class);
    CARE_TYPES.add(BigDecimal.class);
    CARE_TYPES.add(Number.class);
    
    CARE_TYPES.add(Character.TYPE);
    CARE_TYPES.add(String.class);
    
    CARE_TYPES.add(Boolean.TYPE);
    CARE_TYPES.add(Boolean.class);
    
    CARE_TYPES.add(java.util.Date.class);
    CARE_TYPES.add(java.sql.Date.class);
    CARE_TYPES.add(Time.class);
    CARE_TYPES.add(Timestamp.class);
  }
  
  
  
  public static String[] split(String str, String regex)
  {
    String[] sa = null;
    if ((str != null) && (notEmpty(regex))) {
      sa = str.split(rex4Str(regex));
    }
    return sa;
  }
  
  public static String rex4Str(String regex)
  {
    String rexc = "";
    if (".".equals(regex)) {
      rexc = "\\.";
    } else if ("^".equals(regex)) {
      rexc = "\\^";
    } else if ("$".equals(regex)) {
      rexc = "\\$";
    } else if ("*".equals(regex)) {
      rexc = "\\*";
    } else if ("+".equals(regex)) {
      rexc = "\\+";
    } else if ("|".equals(regex)) {
      rexc = "\\|";
    } else {
      rexc = regex;
    }
    return rexc;
  }
  
  public static String null2EmptyString(Object o)
  {
    return o == null ? "" : o.toString();
  }
  
  
  public static String printStackTrace2String(Throwable t)
  {
    String exceMsg = null;
    StringWriter sw = null;
    PrintWriter ps = null;
    try
    {
      sw = new StringWriter();
      ps = new PrintWriter(sw);
      t.printStackTrace(ps);
      exceMsg = sw.toString();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      if (ps != null) {
        ps.close();
      }
      if (sw != null) {
        try
        {
          sw.close();
        }
        catch (IOException localIOException) {}
      }
    }
    finally
    {
      if (ps != null) {
        ps.close();
      }
      if (sw != null) {
        try
        {
          sw.close();
        }
        catch (IOException localIOException1) {}
      }
    }
    return exceMsg;
  }
  
  private static String POSTFIX_ZIP = ".zip";
  
  public static String getFileNameFromFilePath(String filePath, boolean isPath2Name)
  {
    String zipFileName = "";
    if (notEmpty(filePath))
    {
      String strTempPath = conversionSpecialCharacters(filePath);
      if (hasSubStrNotInitialOrLast(zipFileName, ".")) {
        zipFileName = filePath.substring(strTempPath
          .indexOf(File.separator));
      } else if (isPath2Name)
      {
        if (strTempPath.endsWith(File.separator))
        {
          String sub = strTempPath.substring(0, strTempPath
            .length() - 1);
          zipFileName = sub.substring(sub
            .lastIndexOf(File.separator) + 1, sub.length());
        }
      }
      else {
        zipFileName = System.currentTimeMillis() + POSTFIX_ZIP;
      }
    }
    return zipFileName;
  }
  
  public static String conversionSpecialCharacters(String zipFileRootPath)
  {
    String strPath = "";
    if (notEmpty(zipFileRootPath))
    {
      strPath = zipFileRootPath.replaceAll("[/(\\\\)]", "/");
      if (!strPath.endsWith(File.separator)) {
        strPath = strPath + File.separator;
      }
    }
    return strPath;
  }
  
  public static String replaceOnce(String template, String placeholder, String replacement)
  {
    String strDes = template;
    if (notEmpty(template))
    {
      int loc = template.indexOf(placeholder);
      if (loc >= 0) {
        strDes = 
          template.substring(0, loc) + replacement + 
          template.substring(loc + placeholder.length());
      }
    }
    return strDes;
  }
  
  public static boolean isSubStrNotInitialOrLast(String str, String subStr)
  {
    return (str != null) && (!str.startsWith(subStr)) && (!str.endsWith(subStr));
  }
  
  public static boolean hasSubStrNotInitialOrLast(String str, String subStr)
  {
    return (str != null) && (str.indexOf(subStr) > 0) && (!str.endsWith(subStr));
  }
  
  private static Random random = new Random(87860988L);
  
  
  public static boolean isNumber(String str)
  {
    if (isEmpty(str)) {
      return false;
    }
    Pattern p = Pattern.compile("\\d*");
    Matcher m = p.matcher(str);
    return m.matches();
  }
  
  public static String joinString(Object... obj)
  {
    StringBuilder sb = new StringBuilder();
    if (obj != null)
    {
      Object[] arrayOfObject = obj;int j = obj.length;
      for (int i = 0; i < j; i++)
      {
        Object o = arrayOfObject[i];
        sb.append(o);
      }
    }
    return sb.toString();
  }
  
  public static String conversionFilePathSpecialCharacters(String filePath)
  {
    String strPath = "";
    if (notEmpty(filePath)) {
      strPath = filePath.replaceAll("[/(\\\\)]", "/");
    }
    return strPath;
  }
  
  public static String toString(Collection<?> coll, char split)
  {
    if (coll == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    boolean begin = true;
    for (Object o : coll) {
      if (o != null)
      {
        if (begin) {
          begin = false;
        } else {
          sb.append(split);
        }
        sb.append(o);
      }
    }
    return sb.toString();
  }
  
  public static String toString(Collection<?> coll)
  {
    return toString(coll, ',');
  }
}
