DROP TABLE IF EXISTS grades;
DROP TABLE IF EXISTS preferences;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS packs;
DROP TABLE IF EXISTS users;


CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL CHECK (role IN ('ROLE_STUDENT', 'ROLE_INSTRUCTOR', 'ROLE_ADMIN'))
);


CREATE TABLE packs (
                       id BIGSERIAL PRIMARY KEY,
                       year INT NOT NULL,
                       semester INT NOT NULL,
                       name VARCHAR(255) NOT NULL
);


CREATE TABLE students (
                          id BIGSERIAL PRIMARY KEY,
                          user_id BIGINT UNIQUE NOT NULL,
                          code VARCHAR(50) UNIQUE NOT NULL,
                          year INT NOT NULL,
                          pack_id BIGINT,

                          CONSTRAINT fk_student_user
                              FOREIGN KEY(user_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE,

                          CONSTRAINT fk_student_pack
                              FOREIGN KEY(pack_id)
                                  REFERENCES packs(id)
                                  ON DELETE SET NULL
);

CREATE TABLE courses (
                         id BIGSERIAL PRIMARY KEY,
                         type VARCHAR(20) NOT NULL CHECK (type IN ('compulsory', 'optional')),
                         code VARCHAR(50) UNIQUE NOT NULL,
                         abbr VARCHAR(20),
                         name VARCHAR(255) NOT NULL,
                         instructor_id BIGINT,
                         pack_id BIGINT,
                         group_count INT DEFAULT 1,
                         description TEXT,

                         CONSTRAINT fk_instructor
                             FOREIGN KEY(instructor_id)
                                 REFERENCES users(id)
                                 ON DELETE SET NULL,

                         CONSTRAINT fk_pack
                             FOREIGN KEY(pack_id)
                                 REFERENCES packs(id)
                                 ON DELETE SET NULL
);
CREATE TABLE preferences (
                             id BIGSERIAL PRIMARY KEY,
                             student_id BIGINT NOT NULL,
                             course_id BIGINT NOT NULL,
                             rank INT NOT NULL,

                             CONSTRAINT fk_preference_student
                                 FOREIGN KEY(student_id)
                                     REFERENCES students(id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_preference_course
                                 FOREIGN KEY(course_id)
                                     REFERENCES courses(id)
                                     ON DELETE CASCADE,

                             CONSTRAINT uk_student_course UNIQUE (student_id, course_id),
                             CONSTRAINT uk_student_rank UNIQUE (student_id, rank)
);

CREATE TABLE grades (
                        id BIGSERIAL PRIMARY KEY,
                        student_id BIGINT NOT NULL,
                        course_id BIGINT NOT NULL,
                        value DOUBLE PRECISION NOT NULL,

                        CONSTRAINT fk_grade_student FOREIGN KEY(student_id) REFERENCES students(id) ON DELETE CASCADE,
                        CONSTRAINT fk_grade_course FOREIGN KEY(course_id) REFERENCES courses(id) ON DELETE CASCADE
    -- one grade per student per course
--                         CONSTRAINT uk_grade_student_course UNIQUE (student_id, course_id)
);