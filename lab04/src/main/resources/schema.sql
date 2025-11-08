-- Drop in reverse order
DROP TABLE IF EXISTS preferences;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS packs;
DROP TABLE IF EXISTS users; -- 'instructors' is now 'users'

-- Create 'users' first (it has no dependencies)
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL CHECK (role IN ('ROLE_STUDENT', 'ROLE_INSTRUCTOR', 'ROLE_ADMIN'))
);

-- Create 'packs' (no dependencies)
CREATE TABLE packs (
                       id BIGSERIAL PRIMARY KEY,
                       year INT NOT NULL,
                       semester INT NOT NULL,
                       name VARCHAR(255) NOT NULL
);

-- Create 'students' (depends on 'users' and 'packs')
CREATE TABLE students (
                          id BIGSERIAL PRIMARY KEY,
                          user_id BIGINT UNIQUE NOT NULL,
                          code VARCHAR(50) UNIQUE NOT NULL,
                          year INT NOT NULL,
                          pack_id BIGINT,

                          CONSTRAINT fk_student_user
                              FOREIGN KEY(user_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE, -- If User is deleted, Student profile is deleted

                          CONSTRAINT fk_student_pack
                              FOREIGN KEY(pack_id)
                                  REFERENCES packs(id)
                                  ON DELETE SET NULL
);

-- Create 'courses' (depends on 'users' and 'packs')
CREATE TABLE courses (
                         id BIGSERIAL PRIMARY KEY,
                         type VARCHAR(20) NOT NULL CHECK (type IN ('compulsory', 'optional')),
                         code VARCHAR(50) UNIQUE NOT NULL,
                         abbr VARCHAR(20),
                         name VARCHAR(255) NOT NULL,
                         instructor_id BIGINT, -- This is now a user_id
                         pack_id BIGINT,
                         group_count INT DEFAULT 1,
                         description TEXT,

                         CONSTRAINT fk_instructor
                             FOREIGN KEY(instructor_id)
                                 REFERENCES users(id) -- References the new 'users' table
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
                                     REFERENCES students(id) -- References 'students' table ID
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_preference_course
                                 FOREIGN KEY(course_id)
                                     REFERENCES courses(id)
                                     ON DELETE CASCADE,

                             CONSTRAINT uk_student_course UNIQUE (student_id, course_id),
                             CONSTRAINT uk_student_rank UNIQUE (student_id, rank)
);