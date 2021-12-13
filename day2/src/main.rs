use regex::Regex;
use std::io;
use std::io::BufRead;

fn main() {
    let stdin = io::stdin();

    let forward_re = Regex::new(r"^forward (\d+)$").unwrap();
    let up_re = Regex::new(r"^up (\d+)$").unwrap();
    let down_re = Regex::new(r"^down (\d+)$").unwrap();

    let mut fwd = 0;
    let mut depth = 0;
    let mut aim = 0;

    for line in stdin.lock().lines() {
        if let Ok(l) = line {
            println!("{}", l);
            if let Some(f) = forward_re.captures(&l) {
                let val = f.get(1).unwrap().as_str().parse::<i32>().unwrap();
                fwd += val;
                depth += val * aim;
            }

            if let Some(u) = up_re.captures(&l) {
                let val = u.get(1).unwrap().as_str().parse::<i32>().unwrap();
                aim -= val;
            }

            if let Some(d) = down_re.captures(&l) {
                let val = d.get(1).unwrap().as_str().parse::<i32>().unwrap();
                aim += val;
            }
        }
        println!("fwd {}, depth {}, aim {}", fwd, depth, aim);
    }

    println!("{}*{}={}", fwd, depth, fwd*depth);
}
