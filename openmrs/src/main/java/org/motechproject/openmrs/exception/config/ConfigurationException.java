package org.motechproject.openmrs.exception.config;

/**
 * Base for all exception related to the configuration errors.
 */
public abstract class ConfigurationException extends RuntimeException {

    ConfigurationException(String message, String... args) {
        super(String.format(message, args));
    }

}
