package util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 判空工具类
 */
public class EmptyUtils {

    // 私有EmptyUtils构造类，让外界不能创建对象，只能静态调用方法
    private EmptyUtils() {
        throw new UnsupportedOperationException("you can't instantiate me...");
    }

    /**
     * 功能描述: Object 等于空
     * @date 2018/11/01 15:06
     * @Param [obj]
     * @return boolean
     */
    public static boolean isEmpty(Object obj){
        return obj==null;
    }


    /**
     * 功能描述: Object 不等于空
     * @date 2018/11/01 15:07
     * @Param [obj]
     * @return boolean
     */
    public static boolean isNotEmpty(Object obj){
        return obj!=null;
    }


    /**
     * 功能描述：Map等于空
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<Object, Object> map) {
        return (map==null || map.size()==0);
    }


    /**
     * 功能描述: Map不等于空
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map<Object, Object> map) {
        return (map!=null && map.size()!=0);
    }

    /**
     * 功能描述: str 等于空
     * @date 2018/11/01 15:03
     * @Param [str]
     * @return boolean
     */
    public static boolean isEmpty(String str){
        return (str==null || "".equals(str) || (str.trim().length() == 0));
    }


    /**
     * 功能描述: str 不等于空
     * @date 2018/11/01 15:05
     * @Param [str]
     * @return boolean
     */
    public static boolean isNotEmpty(String str){
        return (str!=null && !str.equals(""));
    }

    /**
     * 功能描述: int数字类型为空
     * @date 2018/11/01 15:25
     * @Param [num]
     * @return boolean
     */
    public static boolean isEmpty(Integer num){
        return num == null;
    }

    /**
     * 功能描述: int数字类型不为空
     * @date 2018/11/01 15:25
     * @Param [num]
     * @return boolean
     */
    public static boolean isNotEmpty(Integer num){
        return num != null;
    }


    /**
     * 功能描述: 判断list集合为空：list == null 或 list.size ==0  返回 true
     * @date 2018/11/01 15:00
     * @Param [list]
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    public static boolean isEmpty(List list){
        return (list==null || list.size()==0);
    }


    /**
     * 功能描述: 判断list集合不为空：list != null 或 list.size >0  返回 true
     * @date 2018/11/01 15:02
     * @Param [list]
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    public static boolean isNotEmpty(List list){
        return (list != null && list.size() > 0);
    }


    /**
     * 功能描述: 对象数组为空
     * @date 2018/11/01 15:09
     * @Param [strings]
     * @return boolean
     */
    public static boolean isEmpty(Object[] obj) {
        return ((obj == null) || (obj.length == 0));
    }


    /**
     * 功能描述: 对象数组不为空
     * @date 2018/11/01 15:23
     * @Param [strings]
     * @return boolean
     */
    public static boolean isNotEmpty(Object[] obj){
        return ((obj != null) && (obj.length > 0));
    }


    /**
     * 功能描述: collection集合为空
     * @date 2018/11/01 15:27
     * @Param [obj]
     * @return boolean
     */
//    public static boolean isEmpty(Collection<?> conn) {
//        return ((conn == null) || (conn.size() <= 0));
//    }

    /**
     * 功能描述: collection集合不为空
     * @date 2018/11/01 15:28
     * @Param [conn]
     * @return boolean
     */
//    public static boolean isNotEmpty(Collection<?> conn) {
//        return ((conn != null) && (conn.size() > 0));
//    }

    /**
     * 功能描述: byte数组为空
     * @date 2018/11/01 15:29
     * @Param [bys]
     * @return boolean
     */
    public static boolean isEmpty(byte[] bys) {
        return ((bys == null) || (bys.length == 0));
    }

    /**
     * 功能描述: byte数组不为空
     * @date 2018/11/01 15:30
     * @Param [bys]
     * @return boolean
     */
    public static boolean isNotEmpty(byte[] bys) {
        return ((bys != null) && (bys.length > 0));
    }

    /**
     * 功能描述: long类型为空
     * @date 2018/11/01 15:30
     * @Param [l]
     * @return boolean
     */
    public static boolean isEmpty(Long l) {
        return ((l == null) || (l.longValue() == 0L));
    }


    /**
     * 功能描述: long类型不为空
     * @date 2018/11/01 15:30
     * @Param [l]
     * @return boolean
     */
    public static boolean isNotEmpty(Long l) {
        return ((l != null) && (l.longValue() > 0L));
    }

}
