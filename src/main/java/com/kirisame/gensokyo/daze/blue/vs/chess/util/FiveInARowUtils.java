package com.kirisame.gensokyo.daze.blue.vs.chess.util;

import com.kirisame.gensokyo.daze.blue.vs.chess.entity.FiveInARowDropPoint;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @auther: MaoHangBin
 * @date: 2019/11/28 16:30
 */

public class FiveInARowUtils {

    private final static int englishLetterStartIndex = 64;
    private final static int doPointLength = 2;
    private final static int maxNeedMore = 4;
    private final static int minPoint = 1;
    private final static int maxPoint = 15;

    public static boolean isWin(String doPoint, String userType, Map<String, String> pointAllMap) {
        FiveInARowDropPoint dropPoint = getDropPoint(doPoint);
        List<String> columnLinkList = getColumnLinkList(dropPoint);
        List<String> rowLinkList = getRowLinkList(dropPoint);
        List<String> leftObliqueLinkList = getLeftObliqueLinkList(dropPoint);
        List<String> rightObliqueLinkList = getRightObliqueLinkList(dropPoint);
        if (checkOneLinkIsWin(columnLinkList, userType, pointAllMap)) {
            return true;
        } else if (checkOneLinkIsWin(rowLinkList, userType, pointAllMap)) {
            return true;
        } else if (checkOneLinkIsWin(leftObliqueLinkList, userType, pointAllMap)) {
            return true;
        } else if (checkOneLinkIsWin(rightObliqueLinkList, userType, pointAllMap)) {
            return true;
        }
        return false;
    }

    private static boolean checkOneLinkIsWin(List<String> pointLinkList, String userType, Map<String, String> pointAllMap) {
        int count = 0;
        for (String point : pointLinkList) {
            String chessType = pointAllMap.get(point);
            if (StringUtils.equals(userType, chessType)) {
                count++;
                if (count == 5) {
                    return true;
                }
            } else {
                count = 0;
            }
        }
        return false;
    }

    public static FiveInARowDropPoint getDropPoint(String doPoint) {
        doPoint = StringUtils.trim(doPoint);
        if (doPoint.length() != doPointLength) {
            return null;
        }
        String column = StringUtils.substring(doPoint, 0, 1);
        String row = StringUtils.substring(doPoint, 1, 2);
        int columnInt = column.charAt(0) - englishLetterStartIndex;
        int rowInt = Integer.parseInt(row);
        FiveInARowDropPoint fiveInARowDropPoint = new FiveInARowDropPoint();
        fiveInARowDropPoint.setColumn(column);
        fiveInARowDropPoint.setRow(row);
        fiveInARowDropPoint.setColumnInt(columnInt);
        fiveInARowDropPoint.setRowInt(rowInt);
        return fiveInARowDropPoint;
    }

    public static List<String> getRowLinkList(FiveInARowDropPoint dropPoint) {
        Integer columnInt = dropPoint.getColumnInt();
        String row = dropPoint.getRow();
        List<String> rowLinkList = new ArrayList<>();
        int minRowPoint = columnInt - maxNeedMore;
        if (minRowPoint < minPoint) {
            minRowPoint = minPoint;
        }
        int maxRowPoint = columnInt + maxNeedMore;
        if (maxRowPoint > maxPoint) {
            maxRowPoint = maxPoint;
        }
        for (int i = minRowPoint; i <= maxRowPoint; i++) {
            String column = String.valueOf((char) (i + englishLetterStartIndex));
            rowLinkList.add(column + row);
        }
        return rowLinkList;
    }

    public static List<String> getColumnLinkList(FiveInARowDropPoint dropPoint) {
        String column = dropPoint.getColumn();
        Integer rowInt = dropPoint.getRowInt();
        List<String> columnLinkList = new ArrayList<>();
        int minColumnPoint = rowInt - maxNeedMore;
        if (minColumnPoint < minPoint) {
            minColumnPoint = minPoint;
        }
        int maxColumnPoint = rowInt + maxNeedMore;
        if (maxColumnPoint > maxPoint) {
            maxColumnPoint = maxPoint;
        }
        for (int i = minColumnPoint; i <= maxColumnPoint; i++) {
            columnLinkList.add(column + i);
        }
        return columnLinkList;
    }

    public static List<String> getRightObliqueLinkList(FiveInARowDropPoint dropPoint) {
        Integer columnInt = dropPoint.getColumnInt();
        Integer rowInt = dropPoint.getRowInt();
        List<String> rightObliqueLinkList = new ArrayList<>();
        int minThanColumn = columnInt - minPoint;
        int minThanRow = rowInt - minPoint;
        int minThanAll = minThanColumn < minThanRow ? minThanColumn : minThanRow;
        if (minThanAll > maxNeedMore) {
            minThanAll = maxNeedMore;
        }
        int maxLessColumn = maxPoint - columnInt;
        int maxLessRow = maxPoint - rowInt;
        int maxLessAll = maxLessColumn < maxLessRow ? maxLessColumn : maxLessRow;
        if (maxLessAll > maxNeedMore) {
            maxLessAll = maxNeedMore;
        }
        List<String> columnLinkList = new ArrayList<>();
        int minColumnPoint = columnInt - minThanAll;
        int maxColumnPoint = columnInt + maxLessAll;
        for (int i = minColumnPoint; i <= maxColumnPoint; i++) {
            columnLinkList.add(String.valueOf((char) (i + englishLetterStartIndex)));
        }
        List<String> rowLinkList = new ArrayList<>();
        int minRowPoint = rowInt - minThanAll;
        int maxRowPoint = rowInt + maxLessAll;
        for (int i = minRowPoint; i <= maxRowPoint; i++) {
            rowLinkList.add(Integer.toString(i));
        }
        for (int i = 0; i < columnLinkList.size(); i++) {
            rightObliqueLinkList.add(columnLinkList.get(i) + rowLinkList.get(i));
        }
        return rightObliqueLinkList;
    }

    public static List<String> getLeftObliqueLinkList(FiveInARowDropPoint dropPoint) {
        Integer columnInt = dropPoint.getColumnInt();
        Integer rowInt = dropPoint.getRowInt();
        List<String> leftObliqueLinkList = new ArrayList<>();
        int minThanColumn = maxPoint - columnInt;
        int minThanRow = rowInt - minPoint;
        int minThanAll = minThanColumn < minThanRow ? minThanColumn : minThanRow;
        if (minThanAll > maxNeedMore) {
            minThanAll = maxNeedMore;
        }
        int maxLessColumn = columnInt - minPoint;
        int maxLessRow = maxPoint - rowInt;
        int maxLessAll = maxLessColumn < maxLessRow ? maxLessColumn : maxLessRow;
        if (maxLessAll > maxNeedMore) {
            maxLessAll = maxNeedMore;
        }
        List<String> columnLinkList = new ArrayList<>();
        int maxColumnPoint = columnInt + minThanAll;
        int minColumnPoint = columnInt - maxLessAll;
        for (int i = maxColumnPoint; i >= minColumnPoint; i--) {
            columnLinkList.add(String.valueOf((char) (i + englishLetterStartIndex)));
        }
        List<String> rowLinkList = new ArrayList<>();
        int minRowPoint = rowInt - minThanAll;
        int maxRowPoint = rowInt + maxLessAll;
        for (int i = minRowPoint; i <= maxRowPoint; i++) {
            rowLinkList.add(Integer.toString(i));
        }
        for (int i = 0; i < columnLinkList.size(); i++) {
            leftObliqueLinkList.add(columnLinkList.get(i) + rowLinkList.get(i));
        }
        return leftObliqueLinkList;
    }

}
