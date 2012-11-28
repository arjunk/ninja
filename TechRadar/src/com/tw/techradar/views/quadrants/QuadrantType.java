package com.tw.techradar.views.quadrants;

public enum QuadrantType {
    QUADRANT_1(1),
    QUADRANT_2(2),
    QUADRANT_3(3),
    QUADRANT_4(4),
    QUADRANT_ALL(0);

    private int quadrantNo;

    QuadrantType(int quadrantNo) {
        this.quadrantNo = quadrantNo;
    }

    public int getQuadrantNo() {
        return quadrantNo;
    }
}
