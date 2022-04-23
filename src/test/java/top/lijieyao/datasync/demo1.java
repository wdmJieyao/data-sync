package top.lijieyao.datasync;

/**
 * @Description:
 * @Author: LiJieYao
 * @Date: 2022/4/22 19:10
 */
public class demo1 {

    public static void main(String[] args) {

        User user = new User(45494);
        System.out.println(Integer.toBinaryString(45494));
        System.out.println(Integer.toBinaryString(512));

        if (user.hasPrivilege()) {
            System.out.println("yes");
        } else {
            System.out.println("no");

        }

    }

    static class User {

        // 删除权限
        private final static int USER_PRIVILEGE_DELETE = 512;

        private Integer accessCode;

        // 判断用户是否拥有删除权限
        public boolean hasPrivilege() {
            if (accessCode == null) {
                return false;
            }


            return (USER_PRIVILEGE_DELETE & accessCode) == accessCode;
        }

        public Integer getAccessCode() {
            return accessCode;
        }

        public void setAccessCode(Integer accessCode) {
            this.accessCode = accessCode;
        }

        public User(Integer accessCode) {
            this.accessCode = accessCode;
        }
    }

}
