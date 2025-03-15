package com.eg.blps1.exceptions;

public class ActiveSanctionException extends CustomException {
    public ActiveSanctionException() {
        super("Вы не можете бронировать помещение из-за санкции");
    }
}
