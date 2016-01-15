package com.talanlabs.component.configuration.factory.tostring;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.configuration.factory.IToStringFactory;
import com.talanlabs.component.configuration.factory.tostring.compare.IPropertyComparator;
import com.talanlabs.component.factory.ComponentDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CompleteToStringFactory implements IToStringFactory {

    public static final IPropertyComparator COMPLETE_PROPERTY_COMPARATOR = IPropertyComparator.compose(IPropertyComparator.equalsKey(), IPropertyComparator.natural());

    private final boolean hideHeader;
    private final boolean multiline;
    private final IPropertyComparator propertyComparator;
    private final boolean include;
    private final Set<String> incExcPropertyNames;
    private final boolean hideNullValue;

    public CompleteToStringFactory() {
        this(false, false, false);
    }

    public CompleteToStringFactory(boolean hideHeader, boolean multiline, boolean hideNullValue) {
        this(hideHeader, multiline, hideNullValue, COMPLETE_PROPERTY_COMPARATOR);
    }

    public CompleteToStringFactory(boolean hideHeader, boolean multiline, boolean hideNullValue, IPropertyComparator propertyComparator) {
        this(hideHeader, multiline, hideNullValue, propertyComparator, true, null);
    }

    public CompleteToStringFactory(boolean hideHeader, boolean multiline, boolean hideNullValue, IPropertyComparator propertyComparator, boolean include, String... incExcPropertyNames) {
        super();

        this.hideHeader = hideHeader;
        this.multiline = multiline;
        this.propertyComparator = propertyComparator;
        this.include = include;
        this.incExcPropertyNames = incExcPropertyNames != null ? new HashSet<>(Arrays.asList(incExcPropertyNames)) : null;
        this.hideNullValue = hideNullValue;
    }

    @Override
    public <E extends IComponent> String buildToString(ComponentDescriptor<E> componentDescriptor, E component) {
        StringBuilder sb = new StringBuilder();

        if (!hideHeader) {
            sb.append(componentDescriptor.getName()).append("@").append(component.hashCode()).append(" {");
        } else {
            sb.append("{");
        }
        if (multiline) {
            sb.append("\n");
        }

        List<String> propertyNames;
        if (this.incExcPropertyNames != null) {
            if (include) {
                propertyNames = new ArrayList<>(incExcPropertyNames);
            } else {
                propertyNames = new ArrayList<>(componentDescriptor.getPropertyNames());
                propertyNames.removeAll(incExcPropertyNames);
            }
        } else {
            propertyNames = new ArrayList<>(componentDescriptor.getPropertyNames());
        }

        if (hideNullValue) {
            Iterator<String> it = propertyNames.iterator();
            while (it.hasNext()) {
                Object value = component.straightGetProperty(it.next());
                if (value == null) {
                    it.remove();
                }
            }
        }

        if (propertyComparator != null) {
            Comparator<String> comparator = propertyComparator.createComparator(componentDescriptor, component);
            Collections.sort(propertyNames, comparator);
        }

        Map<String, Object> valueMap = component.straightGetProperties();
        sb.append(propertyNames.stream().map(propertyName -> buildPropertyString(propertyName, valueMap.get(propertyName))).collect(Collectors.joining("," + (multiline ? "\n" : ""))));

        if (multiline) {
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    private String buildPropertyString(String propertyName, Object value) {
        String text = null;
        if (value != null) {
            if (value.getClass().isArray()) {
                Object[] objectArray = { value };
                String arrayString = Arrays.deepToString(objectArray);
                text = arrayString.substring(1, arrayString.length() - 1);
            } else {
                text = value.toString();
                if (value instanceof String) {
                    text = "\"" + text + "\"";
                }
            }
            if (multiline && text != null) {
                text = text.replaceAll("\n", "\n\t");
            }
        }
        return (multiline ? "\t" : "") + "\"" + propertyName + "\" : " + text;
    }
}
