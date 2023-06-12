package com.sparta.springprepare;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Memo {
    private String username;
    private String contents;
}

class Main {
    public static void main(String[] args) {
        Memo memo = new Memo();
        memo.setUsername("KGJ");
        System.out.println(memo.getUsername());
    }
}