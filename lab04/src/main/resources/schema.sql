DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS packs;
DROP TABLE IF EXISTS instructors;
DROP TABLE IF EXISTS students;

CREATE TABLE students (
                          id BIGSERIAL PRIMARY KEY,
                          code VARCHAR(50) UNIQUE NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) UNIQUE NOT NULL,
                          year INT NOT NULL
);


CREATE TABLE instructors (
                             id BIGSERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE packs (
                       id BIGSERIAL PRIMARY KEY,
                       year INT NOT NULL,
                       semester INT NOT NULL,
                       name VARCHAR(255) NOT NULL
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
                                 REFERENCES instructors(id)
                                 ON DELETE SET NULL,

                         CONSTRAINT fk_pack
                             FOREIGN KEY(pack_id)
                                 REFERENCES packs(id)
                                 ON DELETE SET NULL
);

-- Index for optimization
-- CREATE INDEX IF NOT EXISTS idx_course_pack ON courses(pack_id);
-- CREATE INDEX IF NOT EXISTS idx_course_instructor ON courses(instructor_id);
-- CREATE INDEX IF NOT EXISTS idx_student_code ON students(code);