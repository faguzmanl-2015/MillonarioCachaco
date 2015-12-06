package co.edu.unal.loteria.form;

/**
 * Pojo representing a profile form on the client side.
 */
public class ProfileForm {
    /**
     * Any string user wants us to display him/her on this system.
     */
    private String displayName;

    /**
     * T shirt size.
     */
    private RolUsuario rolUsuario;

    private ProfileForm () {}

    /**
     * Constructor for ProfileForm, solely for unit test.
     * @param displayName A String for displaying the user on this system.
     * @param notificationEmail An e-mail address for getting notifications from this system.
     */
    public ProfileForm(String displayName, RolUsuario rolUsuario) {
        this.displayName = displayName;
        this.rolUsuario = rolUsuario;
    }

    public String getDisplayName() {
        return displayName;
    }

    public RolUsuario getTeeShirtSize() {
        return rolUsuario;
    }
    
    public static enum RolUsuario {
    	NOT_SPECIFIED,
        Cliente,
        Administrador
    }
}
