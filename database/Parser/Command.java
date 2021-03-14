package Parser;

public enum Command {
    //Commands
    USE,
    CREATE_TABLE,
    CREATE_DATABASE,
    DROP,
    ALTER,
    INSERT,
    SELECT,
    UPDATE,
    DELETE,
    JOIN,
    NO_COMMAND,


    //Error Codes to be returned from the Parser
    MISSING_SEMI_COLON,
    TABLE_NAME_TAKEN;
}
