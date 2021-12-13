use std::io;
use std::io::BufRead;

fn most_common(inp: &Vec<u32>) -> u32 {
    let one_count = inp.iter().filter(|&n| *n != 0).count();
    if one_count >= (inp.len() + 1) / 2 {
        1
    } else {
        0
    }
}

fn most_common2(inp: &Vec<u32>) -> u32 {
    let zero_count = inp.iter().filter(|&n| *n == 0).count();
    if zero_count >= (inp.len() + 1) / 2 {
        0
    } else {
        1
    }
}

fn get_single_bits(inp: &Vec<u32>, bit: u32) -> Vec<u32> {
    return inp.iter().map(|n| (n >> bit) & 1).collect();
}

fn filter_by_bit(inp: &Vec<u32>, bit: u32, value: u32) -> Vec<u32> {
    return inp.iter().filter(|&n| (n >> bit) & 1 == value).cloned().collect();
}

fn main() {
    let stdin = io::stdin();

    let values: Vec<u32> = stdin.lock().lines().map(|line| u32::from_str_radix(&line.unwrap(), 2).unwrap()).collect();
    let mut res: u32 = 0;

    let mut i = 0;
    let mut xorrer = 0;
    loop {
        let x: u32 = values.iter().map(|v| 1 & (v >> i)).sum();
        if x == 0 {
            break;
        }
        println!("i={} sum={}", i, x);
        if x >= (values.len() as u32) / 2 {
            res |= 1 << i;
        }

        i += 1;
        xorrer <<= 1;
        xorrer |= 1;
    }

    println!("gamma={} epsilon={} xorrer={} power={}", res, res ^ xorrer, xorrer, res * (res ^ xorrer));

    let mut val2 = values.clone();
    for b in (0..12).rev() {
        println!(">>>>");
        for nnn in &val2 {
            println!("{:8b}", nnn);
        }
        println!("<<<<");
        if val2.len() == 0 {
            println!("Not found");
            break;
        }
        if val2.len() == 1 {
            println!("Found value: {}", val2[0]);
            break;
        }
        println!("b={}", b);
        let m = most_common(&get_single_bits(&val2, b));
        println!("most common={}", m);
        val2 = filter_by_bit(&val2, b, m);
        if val2.len() == 0 {
            println!("Not found");
            break;
        }
        if val2.len() == 1 {
            println!("Found value: {}", val2[0]);
            break;
        }
    }

    let mut val3 = values.clone();
    for b in (0..12).rev() {
        if val3.len() == 0 {
            println!("Not found");
            break;
        }
        if val3.len() == 1 {
            println!("Found value: {}", val3[0]);
            break;
        }
        println!("b={}", b);
        let m = most_common2(&get_single_bits(&val3, b));
        println!("most common={}", m);
        val3 = filter_by_bit(&val3, b, m);
        if val3.len() == 0 {
            println!("Not found");
            break;
        }
        if val3.len() == 1 {
            println!("Found value: {}", val3[0]);
            break;
        }
    }
}
