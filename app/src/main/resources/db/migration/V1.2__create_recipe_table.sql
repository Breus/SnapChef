CREATE TABLE recipe
(
    id               BIGSERIAL PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    description      TEXT,
    num_servings     INTEGER      NOT NULL,
    preparation_time VARCHAR CHECK (preparation_time IN
                                    ('MIN_0_15', 'MIN_15_30', 'MIN_30_45', 'MIN_45_60', 'HOUR_PLUS')),
    author_id        BIGINT       REFERENCES user_account (id) ON DELETE SET NULL
);