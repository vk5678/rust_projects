use std::{
    fs::File,
    io::{self, Read},
};

fn read_username_from_file() -> Result<String, io::Error> {
    let mut string: String = String::new();
    File::open("hello.txt")?.read_to_string(&mut string)?;
    Ok(string)
}
fn main() {
    println!("{:?}", read_username_from_file());
}
