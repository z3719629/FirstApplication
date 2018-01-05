package com.crm.firstapplication.pojo;

import java.util.List;

public class DocPages {

    /**
     * レイヤー情報
     */
    private List<PageLayer> layers;

    public DocPages() {
    }


    public List<PageLayer> getLayers() {
        return layers;
    }

    public void setLayers(List<PageLayer> layers) {
        this.layers = layers;
    }
}
