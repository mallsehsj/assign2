/*
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 *
 */

// Write your code here
package com.example.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.school.model.*;

import com.example.school.repository.StudentRepository;

@Service
public class StudentH2Service implements StudentRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Student> getAllStudents() {
        List<Student> list = db.query("select * from student ", new StudentRowMapper());
        ArrayList<Student> arraylist = new ArrayList<>(list);
        return arraylist;
    }

    @Override
    public Student addStudents(Student student) {
        db.update("insert into student(studentName, gender, standard) values(?,?,?)", student.getStudentName(),
                student.getGender(), student.getStandard());
        return db.queryForObject("select * from student where studentid=?", new StudentRowMapper(),
                student.getStudentId());

    }

    @Override
    public String addBulkStudents(ArrayList<Student> students) {
        int n = students.size();
        for (int i = 0; i < n; i++) {
            db.update("insert into student(studentName, gender, standard) values(?,?,?)",
                    students.get(i).getStudentName(),
                    students.get(i).getGender(), students.get(i).getStandard());
        }
        String k = String.format("Successfully added %d students", n);
        return k;

    }

    @Override
    public Student getById(int studentId) {
        try {
            Student student1 = db.queryForObject("select * from student where studentid=?", new StudentRowMapper(),
                    studentId);
            return student1;

        } catch (Exception w) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        }

    }

    @Override
    public void deleteStudent(int studentId) {
        db.update("delete from student where studentid=?", studentId);
    }

    @Override
    public Student updatStudent(int studentId, Student student) {
        if (student.getStudentName() != null) {
            db.update("update student set studentName=? where studentid=?", student.getStudentName(), studentId);
        }
        if (student.getGender() != null) {
            db.update("update student set gender=? where studentid=?", student.getGender(), studentId);
        }
        if (student.getStandard() != 0) {
            db.update("update student set standard=? where studentid=?", student.getStandard(), studentId);
        }
        try {
            return db.queryForObject("select * from student where studentid=?", new StudentRowMapper(),
                    studentId);

        } catch (Exception s) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        }

    }

}
