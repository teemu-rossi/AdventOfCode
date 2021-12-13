use std::io::{self, BufRead};

fn main() {
    let stdin = io::stdin();

    let values: Vec<i32> = stdin.lock().lines().map(|line| line.unwrap().parse().unwrap()).collect();
    let mut prev_sum: Option<i32> = None;
    let mut count = 0;
    for win in values.windows(3) {
        let sum = win.iter().sum();
        if prev_sum.is_some() && sum > prev_sum.unwrap() {
            count += 1;
        }
        prev_sum = Some(sum);
    }

    println!("inc: {}", count);
}
