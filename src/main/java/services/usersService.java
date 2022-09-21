package services;

import com.google.gson.Gson;
import model.User;
import utils.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class usersService {
    PreparedStatement pst = null;

    public String findAll() {
        List<User> list = new ArrayList<>();
        String json;

        String sql = "SELECT * FROM tb_users";
        Connection conn = DB.Conn();

        try {
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                User us = new User();
                us.setId(rs.getInt("id"));
                us.setName(rs.getString("name"));
                us.setAge(rs.getInt("age"));
                list.add(us);
            }
            rs.close();
            pst.close();

            Gson gson = new Gson();
            json = gson.toJson(list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    public String findById(int id) {
        String json;

        String sql = "SELECT * FROM tb_users WHERE id=?";
        Connection conn = DB.Conn();

        try {
            pst = conn.prepareStatement(sql);
            pst.setInt(1,id);
            ResultSet rs = pst.executeQuery();
            User us = new User();
            while (rs.next()) {
                us.setId(rs.getInt("id"));
                us.setName(rs.getString("name"));
                us.setAge(rs.getInt("age"));
            }

            rs.close();
            pst.close();

            Gson gson = new Gson();

            if (us.getId() == 0) {
                json = "Not found.";
            } else {
                json = gson.toJson(us);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    public boolean create(String payload) {
        if(payload == null) return false;

        Gson gson = new Gson();
        User us = gson.fromJson(payload, User.class);

        Connection conn = DB.Conn();
        String sql = "INSERT INTO tb_users(name,age) VALUES(?,?)";

        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, us.getName());
            pst.setInt(2,us.getAge());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public String update(String payload, int id) {
        String json;
        Gson gson = new Gson();
        User us = gson.fromJson(payload, User.class);

        String sql = "UPDATE tb_users SET name=?, age=? WHERE id=?";
        Connection conn = DB.Conn();

        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, us.getName());
            pst.setInt(2,us.getAge());
            pst.setInt(3,id);
            pst.executeUpdate();
            pst.close();

            json = findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

}
