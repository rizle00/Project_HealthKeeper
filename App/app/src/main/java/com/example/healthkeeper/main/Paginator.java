package com.example.healthkeeper.main;

import java.util.ArrayList;
import java.util.List;

public class Paginator {
    private static final int ITEMS_PER_PAGE = 10; // 한 페이지당 아이템 수
    private int currentPage = 0; // 현재 페이지

    // 데이터를 가져오는 메서드
    public List<String> fetchData() {
        // 실제 데이터를 가져오는 로직을 여기에 구현 (테스트용으로 간단한 문자열 리스트 사용)
        List<String> data = new ArrayList<>();
        for (int i = currentPage * ITEMS_PER_PAGE; i < (currentPage + 1) * ITEMS_PER_PAGE; i++) {
            data.add("Item " + (i + 1));
        }
        return data;
    }

    // 다음 페이지로 이동하는 메서드
    public void loadNextPage() {
        currentPage++;
    }

    // 현재 페이지 리셋
    public void resetPage() {
        currentPage = 0;
    }
}
