package sansan.sentix.Utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {

    // xử lý keyword để tìm kiếm
    public static String normalizeKeyword(String input) {
        if (org.apache.commons.lang3.StringUtils.isBlank(input)) {
            return "";
        }
        // Tách ký tự gốc và dấu
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        // Xóa dấu (các ký tự tổ hợp)
        String withoutDiacritics = normalized.replaceAll("\\p{M}", "");

        // Xử lý riêng cho đ/Đ
        withoutDiacritics = withoutDiacritics.replace("đ", "d").replace("Đ", "D");

        return withoutDiacritics.toUpperCase();
    }

    public static String appenString(Object... args) {
        StringBuilder sb = new StringBuilder().append("");
        for (Object arg : args) {
            if (ObjectUtils.isEmpty(arg)) continue;
            sb.append(arg).append(" ");
        }
        return sb.toString();
    }

    // viết hoa chữ cái đầu
    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // Viết hoa chữ cái đầu của mỗi từ
    public static String capitalizeWords(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                result.append(c);
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }

        return result.toString();
    }

    public static List<String> splitSchedulerDailyVibe(String input) {
        if (ObjectUtils.isEmpty(input)) {
            return List.of();
        }
        return Arrays.stream(input.split("-")).map(String::trim).filter(h -> h.matches("\\d{2}")).collect(Collectors.toList());
    }

    public static String sha256Hex(String title) {
       return DigestUtils.sha256Hex(title).toUpperCase();
    }
}
