package io.solo.squash.ui;

import javax.swing.*;

public class ApplicationConfigGUI {
    private JPanel panelRoot;
    private JTextField textKubectlPath;
    private JTextField textKubectlProxy;
    private JTextField textSquashPath;
    private JTextField textSquashURL;

    public ApplicationConfigGUI() {}

    public JPanel getPanelRoot() {
        return panelRoot;
    }

    public String getTextKubectlPath() {
        return textKubectlPath.getText();
    }

    public void setTextKubectlPath(String textKubectlPath) {
        this.textKubectlPath.setText(textKubectlPath);
    }

    public String getTextKubectlProxy() {
        return textKubectlProxy.getText();
    }

    public void setTextKubectlProxy(String textKubectlProxy) {
        this.textKubectlProxy.setText(textKubectlProxy);
    }

    public String getTextSquashPath() {
        return textSquashPath.getText();
    }

    public void setTextSquashPath(String textSquashPath) {
        this.textSquashPath.setText(textSquashPath);
    }

    public String getTextSquashURL() {
        return textSquashURL.getText();
    }

    public void setTextSquashURL(String textSquashURL) {
        this.textSquashURL.setText(textSquashURL);
    }
}
