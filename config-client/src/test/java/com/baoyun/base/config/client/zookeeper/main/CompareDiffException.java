package com.baoyun.base.config.client.zookeeper.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompareDiffException extends Exception {

    static final Logger log = LoggerFactory.getLogger(CompareDiffException.class);

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CompareDiffException(int target, int current, String message) {
        super(message);
        log.error(" target:{} current:{} message:{} not the same", target, current, message);
    }

    public CompareDiffException(long target, long current, String message) {
        super(message);
        log.error(" target:{} current:{} message:{} not the same", target, current, message);
    }

    public CompareDiffException(String target, String current, String message) {
        super(message);
        log.error(" target:{} current:{} message:{} not the same", target, current, message);
    }

    public CompareDiffException(boolean target, boolean current, String message) {
        super(message);
        log.error(" target:{} current:{} message:{} not the same", target, current, message);
    }

    /**
     * 对比 string
     * 
     * @param target
     * @param current
     * @throws CompareDiffException
     */
    public static void compare(String target, String current) throws CompareDiffException {
        if (target == null && current == null) {
        } else if (target == null && current != null) {
            throw new CompareDiffException(target, current, "not same string ");
        } else if (target != null && current == null) {
            throw new CompareDiffException(target, current, "not same string ");
        } else if (!target.equals(current)) {
            throw new CompareDiffException(target, current, "not same string ");
        }
    }

    /**
     * 对比 booleam
     * 
     * @param target
     * @param current
     * @throws CompareDiffException
     */
    public static void compare(boolean target, boolean current) throws CompareDiffException {
        if (target != current) {
            throw new CompareDiffException(target, current, " not the same boolean ");
        }
    }

    /**
     * 对比 long
     * 
     * @param target
     * @param current
     * @throws CompareDiffException
     */
    public static void compare(long target, long current) throws CompareDiffException {
        if (target != current) {
            throw new CompareDiffException(target, current, "not the same long");
        }
    }

    /**
     * 对比 int
     * 
     * @param target
     * @param current
     * @throws CompareDiffException
     */
    public static void compare(int target, int current) throws CompareDiffException {
        if (target != current) {
            throw new CompareDiffException(target, current, " not the same int ");
        }
    }

}
