use std::io::{self, BufRead};

fn main() {
    let stdin = io::stdin();
    let mut prev: i32 = 0;
    let mut inc: i32 = 0;
    for line in stdin.lock().lines() {
        let val = line.unwrap().parse::<i32>().unwrap();
        if prev != 0 && prev < val {
            inc += 1;
        }
        prev = val;
    }
    println!("inc: {}", inc);
}
