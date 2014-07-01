/**
 * Copyright (c) 2014, OpenMapFX
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of LodgON, the website lodgon.com, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL LODGON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lodgon.openmapfx.service.miataru;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import static org.lodgon.openmapfx.service.miataru.MiataruService.RESOURCES;

/**
 *
 * @author joeri
 */
public class DeviceListCell extends ListCell<Device> {

    private final Image removeImage = new Image(this.getClass().getResourceAsStream(RESOURCES + "/icons/remove.png"));
    private final Image historyImage = new Image(this.getClass().getResourceAsStream(RESOURCES + "/icons/history.png"));

    private final Label label;
    private final HBox graphic;

    public DeviceListCell(Communicator communicator, Model model) {
        ImageView removeImageView = new ImageView(removeImage);
        removeImageView.setFitHeight(12.0);
        removeImageView.setFitWidth(12.0);

        ImageView historyImageView = new ImageView(historyImage);
        historyImageView.setFitHeight(12.0);
        historyImageView.setFitWidth(12.0);

        Button remove = new Button("", removeImageView);
        remove.setOnAction(e -> {
            model.trackingDevices().remove(getItem());
        });

        Button history = new Button("", historyImageView);
        history.setOnAction(e -> {
            communicator.retrieveHistory(getItem());
        });

        label = new Label();

        graphic = new HBox(5, remove, history, label);
        graphic.setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(Device item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {
            label.setText(item.getName());

            setGraphic(graphic);
        } else {
            setText(null);
            setGraphic(null);
        }
    }

}
