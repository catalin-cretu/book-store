create table authors (
    id bigint auto_increment,
    name varchar not null
);

create table categories (
    id bigint auto_increment,
    title varchar not null
);

create table books (
    id bigint auto_increment,
    name varchar not null,
    isbn varchar not null,
    created_timestamp datetime not null,
    updated_timestamp datetime not null,
    author_id bigint,
    foreign key (author_id) references authors (id)
);

create table books_categories (
  book_id bigint not null,
  category_id bigint not null,
  foreign key (book_id) references books (id),
  foreign key (category_id) references categories (id)
);