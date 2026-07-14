package com.photoreview.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.access.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFound(ResourceNotFoundException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorStatus", 404);
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDenied(AccessDeniedException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorStatus", 403);
        mav.addObject("errorMessage", "Access Denied: You do not have permission to access this page.");
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralException(Exception ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorStatus", 500);
        mav.addObject("errorMessage", "An unexpected error occurred. Please contact the administrator.");
        ex.printStackTrace(); // Output details to logs for debugging
        return mav;
    }
}
