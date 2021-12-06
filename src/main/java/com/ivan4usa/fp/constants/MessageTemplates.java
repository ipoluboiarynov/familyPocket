package com.ivan4usa.fp.constants;

/**
 * Class with templates of messages
 */
public class MessageTemplates {

    /**
     * Not match Message
     * @param anyClass any Class
     * @param userId user id
     * @return
     */
    public static String notMatchMessage(Class<?> anyClass, Long userId) {
        return anyClass.getName() + " is not match to user id of " + userId;
    }

    /**
     * Missed id message
     * @param anyClass any Class
     * @return string
     */
    public static String missedIdMessage(Class<?> anyClass) {
        return  anyClass.getName() + " id missed";
    }

    /**
     * Not found Message
     * @param anyClass any Class
     * @param id of object
     * @return string
     */
    public static String notFoundMessage(Class<?> anyClass, Long id) {
        return anyClass.getName() + " with id of " + id + " not found";
    }

    /**
     * Id must be null Message
     * @param anyClass any Class
     * @return string
     */
    public static String idMustBeNull(Class<?> anyClass) {
        return anyClass.getName() + " id must be null";
    }

    /**
     * Empty field Message
     * @param anyClass any Class
     * @return string
     */
    public static String emptyFields(Class<?> anyClass) {
        return "Some fields of " + anyClass.getName() + " are empty";
    }

    /**
     * Id is null Message
     * @param anyClass any Class
     * @return string
     */
    public static String idIsNull(Class<?> anyClass) {
        return anyClass.getName() + " id is null";
    }

    /**
     * email exists Message
     * @param email email address
     * @return string
     */
    public static String emailAlreadyExists(String email) {
        return "User with " + email + " email already exists";
    }

    /**
     * Email is missing Message
     * @return string
     */
    public static String emailIsMissing() {
        return "Email is missing";
    }

    /**
     * Wrong password Message
     * @return string
     */
    public static String wrongPassword() {
        return "Password is not acceptable";
    }
}
