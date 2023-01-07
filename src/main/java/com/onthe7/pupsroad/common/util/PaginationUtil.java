package com.onthe7.pupsroad.common.util;

import com.onthe7.pupsroad.common.domain.dto.Cursor;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.onthe7.pupsroad.common.util.EncryptionUtils.twoWayDecryption;
import static com.onthe7.pupsroad.common.util.EncryptionUtils.twoWayEncryption;
import static com.querydsl.core.types.ConstantImpl.create;
import static com.querydsl.core.types.dsl.Expressions.*;
import static com.querydsl.core.types.dsl.StringExpressions.lpad;

public class PaginationUtil {
    private static final String                     ENCRYPTION_CURSOR_KEY       =   "yeonho0102";
    private static final String                     ENCRYPTION_CURSOR_SALT      =   "1GdvSUveifDSAGtbms32Af3a";

    public static final Integer                     DEFAULT_LIMIT               =   30;
    public static final String                      DEFAULT_CURSOR              =   "999999999999999999999999";
    public static final StringExpression            DEFAULT_CURSOR_ID_PART      =   asString("9999999999");
    public static final NumberExpression<Long>      MAX_CURSOR                  =   asNumber(99999999999999L);
    public static final StringExpression            EMPTY_STRING_EXPRESSION     =   asString("");

    public static StringExpression getRecentDataFirstCursor(DateTimePath<LocalDateTime> datetime, NumberPath<Long> id) {
        return combineCursor(getDatePartFormat(datetime), id);
    }

    public static StringExpression getOlderDataFirstCursor(DateTimePath<LocalDateTime> datetime, NumberPath<Long> id ) {
        NumberTemplate<Long> datePart = numberTemplate(Long.class, "{0}", getDatePartFormat(datetime));
        return combineCursor(numberOperation(Long.class, Ops.SUB, MAX_CURSOR, datePart), id);
    }

    public static StringExpression getSmallerValueFirstCursor(NumberPath<Long> id, NumberPath<? extends Number> value) {
        return combineCursor(numberOperation(Long.class, Ops.SUB, MAX_CURSOR, value), id);
    }

    public static StringExpression getLargeValueFirstCursor(NumberPath<Long> id, NumberPath<? extends Number> value) {
        return combineCursor(value, id);
    }

    public static StringExpression getLargeValueFirstCursor(NumberPath<Long> id, NumberPath<? extends Number> value, Integer decimalPlaces) {
        return combineCursor(convertFloatToCursorFormatNumber(value, decimalPlaces), id);
    }

    public static StringExpression getLargeValueFirstCursor(NumberPath<Long> id, Integer decimalPlaces, List<NumberPath<? extends Number>> values) {
        return combineCursor(getFrontPartFormat(decimalPlaces, values), id);
    }

    public static StringExpression getLargeValueFirstCursor(NumberPath<Long> id, EnumExpression<?>... params) {
        return combineCursor(getFrontPartFormat(params), id);
    }

    public static StringExpression getLargeValueFirstCursor(NumberPath<Long> id, Expression<?> subQuery) {
        return lpad(stringTemplate("{0}", subQuery), 14, '0').concat(getIdPartFormat(id));
    }

    private static StringExpression getFrontPartFormat(Integer decimalPlaces, List<NumberPath<? extends Number>> values) {
        StringExpression cursor = EMPTY_STRING_EXPRESSION;
        for (NumberPath<? extends Number> value : values) {
            NumberExpression<Long> convertedValue = convertFloatToCursorFormatNumber(value, decimalPlaces);
            cursor = cursor.concat(convertedValue.floor().stringValue());
        }
        return cursor;
    }

    private static StringExpression getFrontPartFormat(EnumExpression<?> ...params) {
        StringExpression frontPart = EMPTY_STRING_EXPRESSION;
        for (EnumExpression<?> param : params) {
            frontPart = frontPart.concat(param.stringValue());
        }
        return frontPart;
    }

    private static StringExpression getDatePartFormat(DateTimePath<LocalDateTime> datetime) {
        StringTemplate stringTemplate = stringTemplate("DATE_FORMAT({0}, {1})", datetime, create("%Y%m%d%H%i%s"));
        return lpad(stringTemplate, 14, '0');
    }

    private static StringExpression getIdPartFormat(NumberPath<Long> id) {
        return lpad(Objects.isNull(id) ? DEFAULT_CURSOR_ID_PART: id.stringValue(), 10, '0');
    }

    private static NumberExpression<Long> convertFloatToCursorFormatNumber(NumberPath<? extends Number> value, Integer decimalPlaces) {
        return numberOperation(Long.class, Ops.MULT, value, asNumber(Math.pow(10, decimalPlaces)));
    }

    private static StringExpression combineCursor(StringExpression frontPart, NumberPath<Long> idPart) {
        return lpad(frontPart, 14, '0').concat(getIdPartFormat(idPart));
    }

    private static StringExpression combineCursor(NumberExpression<?> frontPart, NumberPath<Long> idPart) {
        return lpad(frontPart.stringValue(), 14, '0').concat(getIdPartFormat(idPart));
    }

    public static String encryptCursor(String value) {
        return twoWayEncryption(value, ENCRYPTION_CURSOR_SALT, ENCRYPTION_CURSOR_KEY);
    }

    public static String decryptCursor(String value) {
        return twoWayDecryption(value, ENCRYPTION_CURSOR_SALT, ENCRYPTION_CURSOR_KEY);
    }

    public static boolean hasNext(List<?> elements, Integer limit) {
        return elements.size() > limit - 1;
    }

    public static String getNextCursor(Boolean hasNext, List<? extends Cursor> elements) {
        return hasNext ? elements.get(elements.size() - 1).getCursor() : null;
    }
}
