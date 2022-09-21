package com.example.demo;

import services.usersService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/UsersServlet/*")
public class UsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        int rc = HttpServletResponse.SC_OK;
        usersService ud = new usersService();
        String result;
        try {
            if (request.getPathInfo() != null && request.getPathInfo().length() > 1) {
                List<String> paths = Arrays.asList(request.getPathInfo().substring(1).split("/"));
                if (paths.size() > 1) {
                    result = "Invalid path";
                    rc = HttpServletResponse.SC_BAD_REQUEST;
                } else {
                    int id = Integer.parseInt(paths.get(0));
                    result = ud.findById(id);
                }
            } else {
                result = ud.findAll();
            }

            if (result == null) {
                rc = HttpServletResponse.SC_NOT_FOUND;
                result = "Data empty.";
            }

            outputResponse(response,result,rc);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        int rc = HttpServletResponse.SC_OK;
        usersService ud = new usersService();
        boolean res = ud.create(body);
        if (!res) {
            rc = HttpServletResponse.SC_BAD_REQUEST;
        }
        outputResponse(response,body,rc);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int rc = HttpServletResponse.SC_OK;
        String result = null;
        if (request.getPathInfo() != null && request.getPathInfo().length() > 1) {
            List<String> paths = Arrays.asList(request.getPathInfo().substring(1).split("/"));
            if (paths.size() > 1) {
                result = "Invalid path";
                rc = HttpServletResponse.SC_BAD_REQUEST;
            } else {
                int id = Integer.parseInt(paths.get(0));
                usersService ud = new usersService();
                if (ud.findById(id) == null) {
                    result = "Not found.";
                    rc = HttpServletResponse.SC_NOT_FOUND;
                } else {
                    String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                    result = ud.update(body, id);
                }
            }
        }
        outputResponse(response,result,rc);
    }

    private void outputResponse(HttpServletResponse response, String payload, int status) {
        response.setHeader("Content-Type","application/json");
        try {
            response.setStatus(status);
            if(payload != null) {
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(payload.getBytes());
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
