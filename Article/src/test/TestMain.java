package test;

import bean.User;
import util.TableUtils;

public class TestMain {
    public static void main(String[] args) {
        String sql = TableUtils.getCreateTableSQl(User.class);
        System.out.println(sql);
    }
}