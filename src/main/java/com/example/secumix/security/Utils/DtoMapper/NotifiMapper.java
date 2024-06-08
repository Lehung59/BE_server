package com.example.secumix.security.Utils.DtoMapper;

import com.example.secumix.security.notify.Notify;
import com.example.secumix.security.store.model.dtos.NotifiDto;
import com.example.secumix.security.user.User;
import org.springframework.stereotype.Component;

@Component
public class NotifiMapper {
    public Notify toEntity(NotifiDto dto) {
        if (dto == null) {
            return null;
        }

        Notify notify = new Notify();
        notify.setNotiId(dto.getNotiId());
        notify.setDescription(dto.getDescription());
        notify.setNotiStatus(dto.isNotiStatus());
        notify.setDeletedNoti(dto.isDeletedNoti());

        // Assuming you have a method to get User by userId
        User user = getUserById(dto.getUserId());
        notify.setUser(user);

        return notify;
    }

    // Convert Notify to NotifiDto
    public NotifiDto toDto(Notify entity) {
        if (entity == null) {
            return null;
        }

        NotifiDto dto = new NotifiDto();
        dto.setNotiId(entity.getNotiId());
        dto.setDescription(entity.getDescription());
        dto.setNotiStatus(entity.isNotiStatus());
        dto.setDeletedNoti(entity.isDeletedNoti());

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }

        return dto;
    }

    // Example method to get User by userId, you should replace this with actual implementation
    private User getUserById(int userId) {
        // Fetch user from the database or service
        User user = new User();
        user.setId(userId);
        return user;
    }
}
