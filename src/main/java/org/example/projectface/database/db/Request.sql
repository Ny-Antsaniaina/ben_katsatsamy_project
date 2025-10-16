CREATE TYPE motif_type_enum AS ENUM ('medical', 'personal', 'academic');
CREATE TYPE status_enum AS ENUM ('present', 'absent', 'late');
CREATE TYPE role_enum AS ENUM ('student', 'teacher', 'director');

CREATE TABLE face (
                      face_id SERIAL PRIMARY KEY,
                      name VARCHAR NOT NULL
);

CREATE TABLE users (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR NOT NULL,
                      role role_enum NOT NULL
);

CREATE TABLE director (
                          id SERIAL PRIMARY KEY,
                          user_id INT UNIQUE NOT NULL,
                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE teacher (
                         teacher_id SERIAL PRIMARY KEY,
                         face_id INT UNIQUE,
                         user_id INT UNIQUE,
                         FOREIGN KEY (face_id) REFERENCES face(face_id) ON DELETE SET NULL,
                         FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE student (
                         std_id SERIAL PRIMARY KEY,
                         face_id INT UNIQUE,
                         user_id INT UNIQUE,
                         FOREIGN KEY (face_id) REFERENCES face(face_id) ON DELETE SET NULL,
                         FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE course (
                        course_id SERIAL PRIMARY KEY,
                        name VARCHAR NOT NULL,
                        date TIMESTAMP NOT NULL,
                        teacher_id INT,
                        FOREIGN KEY (teacher_id) REFERENCES teacher(teacher_id) ON DELETE SET NULL
);

CREATE TABLE attendance (
                            attendance_id SERIAL PRIMARY KEY,
                            student_id INT NOT NULL,
                            course_id INT NOT NULL,
                            date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            status status_enum DEFAULT 'absent',
                            verify_by VARCHAR,
                            FOREIGN KEY (student_id) REFERENCES student(std_id) ON DELETE CASCADE,
                            FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE
);

CREATE TABLE motif (
                       motif_id SERIAL PRIMARY KEY,
                       type motif_type_enum NOT NULL,
                       description VARCHAR,
                       certified BOOLEAN DEFAULT FALSE,
                       student_id INT,
                       FOREIGN KEY (student_id) REFERENCES student(std_id) ON DELETE CASCADE
);