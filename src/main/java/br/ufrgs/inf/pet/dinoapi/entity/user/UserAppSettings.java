package br.ufrgs.inf.pet.dinoapi.entity.user;

import br.ufrgs.inf.pet.dinoapi.constants.AppSettingsConstants;
import br.ufrgs.inf.pet.dinoapi.enumerable.ColorTheme;
import br.ufrgs.inf.pet.dinoapi.enumerable.FontSize;

import javax.persistence.*;

import static br.ufrgs.inf.pet.dinoapi.constants.AuthConstants.LANGUAGE_MAX;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "user_app_settings")
public class UserAppSettings {

    private static final String SEQUENCE_NAME = "user_app_settings_seq";

    public static final Long DEFAULT_VERSION = 0L;

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "language", length = LANGUAGE_MAX, nullable = false)
    private String language;

    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "color_theme", nullable = false)
    private Integer colorTheme;

    @Column(name = "font_size", nullable = false)
    private Integer fontSize;

    @Column(name = "load_essential_contacts", nullable = false)
    private boolean essentialContactGrant;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserAppSettings() {}

    public UserAppSettings(User user) {
        this.user = user;
        this.version = DEFAULT_VERSION;
        this.colorTheme = ColorTheme.DEVICE.getValue();
        this.fontSize = FontSize.DEFAULT.getValue();
        this.language = AppSettingsConstants.DEFAULT_LANGUAGE;
        this.essentialContactGrant = AppSettingsConstants.DEFAULT_E_CONTACTS_GRANT;;
    }

    public Long getId() {
        return id;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getVersion() {
        return version;
    }

    public void updateVersion() {
        this.version = version + 1l;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(Integer colorTheme) {
        this.colorTheme = colorTheme;
    }

    public boolean getEssentialContactGrant() {
        return essentialContactGrant;
    }

    public void setEssentialContactGrant(boolean essentialContactGrant) {
        this.essentialContactGrant = essentialContactGrant;
    }
}
