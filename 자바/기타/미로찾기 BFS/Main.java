import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Main {

    private static int x = 0;      // 좌, 우
    private static int y = 1;      // 상, 하

    private static int load = 1;   // 길
    private static int end = 2;    // 도착

    // 지도
    private static int[][] map = {
            // 0  1  2  3  4  5
            {1, 1, 1, 1, 1, 1}, // 0
            {1, 0, 1, 0, 1, 0}, // 1
            {0, 0, 1, 0, 1, 0}, // 2
            {2, 0, 0, 0, 1, 1}, // 3
            {1, 1, 0, 0, 1, 0}, // 4
            {0, 1, 1, 1, 1, 1}  // 5
    };

    private static int mapXsize = map[0].length;   // 지도 최대 넓이 (좌, 우)
    private static int mapYsize = map.length;      // 지도 최대 높이 (상, 하)

    private static boolean[][] vistedMap = new boolean[map.length][map[0].length]; // 왔던 길 체크하기

    private static Stack<int[]> oldLoad = new Stack<>(); // 돌아가는길

    private static int[] xMove = {0, 0, 1, -1}; // x 축 이동 범위
    private static int[] yMove = {-1, 1, 0, 0}; // y 축 이동 범위
    private static int moveRange = xMove.length; // 이동 범위

    private static boolean isEnd = false;

    public static void main(String[] args) {

        System.out.println("지도 크기: " + mapXsize + " x " + mapYsize + "\n");

        Map<String, Object> mapInfo = startMapXy(map, x, y);

        if (mapInfo.get("check").equals(true)) {
            vistedMap[y][x] = true;
            System.out.println("미로 시작 || x: " + mapInfo.get("x") + ", y: " + mapInfo.get("y") + "\n");
        } else {
            System.out.println("시작할 수 없는 위치 입니다. x: " + mapInfo.get("x") + ", y: " + mapInfo.get("y"));
        }

        while(!isEnd) {
            boolean moved = false;

            if(isEnd) break;

            for (int i = 0; i < moveRange; i++) {
                int my = y + yMove[i];  // 이동할 y축
                int mx = x + xMove[i];  // 이동할 x축
                boolean isMapArea = 0 <= my && my < mapYsize && 0 <= mx && mx < mapXsize; // 맵 영역 확인

                // 맵 영역 확인
                if (!isMapArea) {
                    continue;
                }

                // 이동
                if ((map[my][mx] == load || map[my][mx] == end) && !vistedMap[my][mx]) {
                    move(my, mx, "front");
                    moved = true;
                    break;
                }

            }

            if (!isEnd && !moved && !oldLoad.isEmpty()) {
                // 사방이 막혀있으면 되돌아가기 (벽 + 이미 왔던 길)
                int[] backload = oldLoad.pop();
                int by = backload[0];
                int bx = backload[1];

                move(by, bx, "back");
            }

        }

    }

    // 미로 시작위치 설정하기
    private static Map<String, Object> startMapXy(int[][] map, int x, int y) {

        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("x", x);
        resultMap.put("y", y);

        if (map[y][x] < 1) {
            resultMap.put("check", false);
            return resultMap;
        } else {
            resultMap.put("check", true);
        }
        return resultMap;
    }

    // 이동하기
    private static void move(int my, int mx, String mode) {

        if (mode.equals("front")) {
            // 앞으로 전진
            vistedMap[my][mx] = true;               // 왔던 길 잠금
            oldLoad.push(new int[]{y, x});          // 왔던 길 넣기
            if(map[my][mx] == end) isEnd = true;    // 도착 유무
        }

        map[y][x] = 1;  // 길로 변환

        y = my; // y 좌표 갱신
        x = mx; // x 좌표 갱신

        map[y][x] = 9;  // 내 현재 위치

        viewMapLog("MAP");    // 지도상에서 내 이동경로 확인하기

    }
    
    // 로그 보기
    private static void viewMapLog(String mode) {
        String moveStr = !isEnd ? "이동" : "도착";

        if (mode.equals("TEXT")) {
            System.out.println("==== " + moveStr + ": [" + y + ", " + x + "]");
        }

        if (mode.equals("MAP")) {
            System.out.println("==== " + moveStr + ": [" + y + ", " + x + "] ====");

            for (int i = 0; i < mapYsize; i++) {

                for (int j = 0; j < mapXsize; j++) {
                    if (j < 1) System.out.print("||   ");

                    switch (map[i][j]) {
                        case 0:
                            System.out.print("0" + " ");
                            break;
                        case 1:
                            System.out.print("1" + " ");
                            break;
                        case 2:
                            System.out.print("2" + " ");
                            break;
                        case 9:
                            System.out.print("*" + " ");
                            break;
                    }

                    if (j == mapXsize - 1) System.out.print("  ||");
                }
                System.out.println("");
            }

            System.out.println("=====================\n");
        }

    }

}
