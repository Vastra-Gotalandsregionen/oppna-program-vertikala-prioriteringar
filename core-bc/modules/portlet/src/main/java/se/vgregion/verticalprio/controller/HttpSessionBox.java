package se.vgregion.verticalprio.controller;

import org.apache.commons.lang.NotImplementedException;

import javax.portlet.PortletSession;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

/**
 * Created by clalu4 on 2014-04-14.
 */
public class HttpSessionBox implements HttpSession {

    private PortletSession impl;

    public HttpSessionBox(PortletSession impl) {
        this.impl = impl;
    }

    @Override
    public long getCreationTime() {
        return impl.getCreationTime();
    }

    @Override
    public String getId() {
        return impl.getId();
    }

    @Override
    public long getLastAccessedTime() {
        return impl.getLastAccessedTime();
    }

    @Override
    public ServletContext getServletContext() {
        throw new NotImplementedException();
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        impl.setMaxInactiveInterval(interval);
    }

    @Override
    public int getMaxInactiveInterval() {
        return impl.getMaxInactiveInterval();
    }

    @Override
    public HttpSessionContext getSessionContext() {
        throw new NotImplementedException();
    }

    @Override
    public Object getAttribute(String name) {
        return impl.getAttribute(name);
    }

    @Override
    public Object getValue(String name) {
        throw new NotImplementedException();
    }

    @Override
    public Enumeration getAttributeNames() {
        return impl.getAttributeNames();
    }

    @Override
    public String[] getValueNames() {
        throw new NotImplementedException();
    }

    @Override
    public void setAttribute(String name, Object value) {
        impl.setAttribute(name, value);
    }

    @Override
    public void putValue(String name, Object value) {
        throw new NotImplementedException();
    }

    @Override
    public void removeAttribute(String name) {
        impl.removeAttribute(name);
    }

    @Override
    public void removeValue(String name) {
        throw new NotImplementedException();
    }

    @Override
    public void invalidate() {
        impl.invalidate();
    }

    @Override
    public boolean isNew() {
        return impl.isNew();
    }
}
