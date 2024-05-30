package com.example.secumix.security.Utils;

import com.example.secumix.security.Exception.CustomException;
import com.example.secumix.security.user.User;
import com.example.secumix.security.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;
@Component
public class UserUtils {
    private final UserRepository userRepository;

    public UserUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static String calculateTimeSinceLastLogout(long lastLogoutTime) {
        // Lấy thời gian hiện tại
        Instant currentTime = Instant.now();

        // Lấy thời gian đăng xuất gần nhất
        Instant lastLogoutInstant = Instant.ofEpochMilli(lastLogoutTime);

        // Tính thời gian đã off
        Duration duration = Duration.between(lastLogoutInstant, currentTime);

        // Nếu thời gian đã off nhỏ hơn 1 giờ, hiển thị dưới dạng phút
        if (duration.toHours() < 1) {
            long minutes = duration.toMinutes();
            return minutes + (minutes == 1 ? " phút" : " phút");
        } else {
            // Nếu thời gian đã off lớn hơn hoặc bằng 1 giờ, hiển thị dưới dạng giờ
            long hours = duration.toHours();
            return hours + (hours == 1 ? " giờ" : " giờ");
        }
    }
    public static String generateTempPwd(int length) {
        String numbers = "012345678";
        char otp[] = new char[length];
        Random getOtpNum = new Random();
        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(getOtpNum.nextInt(numbers.length()));
        }
        String optCode = "";
        for (int i = 0; i < otp.length; i++) {
            optCode += otp[i];
        }
        return optCode;
    }
//    public static Date getCurrentDay() {
//        Date currentDate = new Date();
//        return new Date(currentDate.getTime());
//    }

    public String getUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"Khong tim thay nguoi dang nhap nay: "+auth.getName()));
        return user.getEmail();
    }
    public int getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).get().getId();
    }

    public static Date getCurrentDay() {
        Date currentDate = new Date();
        return new Date(currentDate.getTime());
    }

}
