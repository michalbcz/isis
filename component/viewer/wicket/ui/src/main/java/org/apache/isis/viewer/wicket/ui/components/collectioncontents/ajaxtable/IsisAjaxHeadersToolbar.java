/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.isis.viewer.wicket.ui.components.collectioncontents.ajaxtable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;


/**
 * Adapted from Wicket's own {@link HeadersToolbar}.
 */
public class IsisAjaxHeadersToolbar<S> extends AbstractToolbar
{
    private static final long serialVersionUID = 1L;

    static abstract class CssAttributeBehavior extends Behavior
    {
        private static final long serialVersionUID = 1L;

        protected abstract String getCssClass();

        /**
         * @see Behavior#onComponentTag(Component, ComponentTag)
         */
        @Override
        public void onComponentTag(final Component component, final ComponentTag tag)
        {
            String className = getCssClass();
            if (!Strings.isEmpty(className))
            {
                tag.append("class", className, " ");
            }
        }
    }

    /**
     * Constructor
     * 
     * @param <T>
     *            the column data type
     * @param table
     *            data table this toolbar will be attached to
     * @param stateLocator
     *            locator for the ISortState implementation used by sortable headers
     */
    public <T> IsisAjaxHeadersToolbar(final DataTable<T, S> table, final ISortStateLocator<S> stateLocator)
    {
        super(table);

        RefreshingView<IColumn<T, S>> headers = new RefreshingView<IColumn<T, S>>("headers")
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected Iterator<IModel<IColumn<T, S>>> getItemModels()
            {
                List<IModel<IColumn<T, S>>> columnsModels = new LinkedList<IModel<IColumn<T, S>>>();

                for (IColumn<T, S> column : table.getColumns())
                {
                    columnsModels.add(Model.of(column));
                }

                return columnsModels.iterator();
            }

            @Override
            protected void populateItem(Item<IColumn<T, S>> item)
            {
                final IColumn<T, S> column = item.getModelObject();

                WebMarkupContainer header = null;

                if (column.isSortable())
                {
                    header = newSortableHeader("header", column.getSortProperty(), stateLocator);
                }
                else
                {
                    header = new WebMarkupContainer("header");
                }

                if (column instanceof IStyledColumn)
                {
                    CssAttributeBehavior cssAttributeBehavior = new CssAttributeBehavior()
                    {
                        private static final long serialVersionUID = 1L;

                        @Override
                        protected String getCssClass()
                        {
                            return ((IStyledColumn<?, S>)column).getCssClass();
                        }
                    };

                    header.add(cssAttributeBehavior);
                }

                item.add(header);
                item.setRenderBodyOnly(true);
                
                Component label;
                //Component checkBox;
                //if(column instanceof ObjectAdapterToggleboxColumn) {
                    label = new Label("label");
                //    checkBox = column.getHeader("checkbox");
                //    label.setVisible(false);
                //} else {
                    label = column.getHeader("label");
                //    checkBox = new CheckBox("checkbox");
                //    checkBox.setVisible(false);
                // }
                header.add(label);
                //header.add(checkBox);
            }
        };
        add(headers);
    }

    /**
     * Factory method for sortable header components. A sortable header component must have id of
     * <code>headerId</code> and conform to markup specified in <code>HeadersToolbar.html</code>
     * 
     * @param headerId
     *            header component id
     * @param property
     *            property this header represents
     * @param locator
     *            sort state locator
     * @return created header component
     */
    protected WebMarkupContainer newSortableHeader(final String headerId, final S property,
        final ISortStateLocator<S> locator)
    {
        return new OrderByBorder<S>(headerId, property, locator)
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSortChanged()
            {
                getTable().setCurrentPage(0);
            }
        };
    }
}
