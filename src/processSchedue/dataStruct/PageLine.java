package processSchedue.dataStruct;

import java.util.Objects;

/**
 * @version v1.0
 * @ClassName: PageTable
 * @Description: 页表
 * @Author: ChenQ
 * @Date: 2021/10/7 on 20:40
 */
public class PageLine {
    private int pageId;//页号
    private int blockId;//块号
    private int p;//状态位
    private int A;//访问字段
    private int M;//修改位
    private int esId;//外存块号

    public PageLine(int pageId, int blockId, int p,int esId) {
        this.pageId = pageId;
        this.blockId = blockId;
        this.p = p;
        this.esId = esId;
    }

    public int getPageId() {
        return pageId;
    }

    public PageLine setPageId(int pageId) {
        this.pageId = pageId;
        return this;
    }

    public int getBlockId() {
        return blockId;
    }

    public PageLine setBlockId(int blockId) {
        this.blockId = blockId;
        return this;
    }

    public int getP() {
        return p;
    }

    public PageLine setP(int p) {
        this.p = p;
        return this;
    }

    public int getEsId() {
        return esId;
    }

    public PageLine setEsId(int esId) {
        this.esId = esId;
        return this;
    }

    @Override
    public String toString() {
        return "PageLine{" +
                "页号=" + pageId +
                ", 块号=" + blockId +
                ", 状态位=" + p +
                ", 外存地址=" + esId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageLine pageLine = (PageLine) o;
        return pageId == pageLine.pageId &&
                blockId == pageLine.blockId &&
                p == pageLine.p &&
                esId == pageLine.esId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageId, blockId, p, esId);
    }
}
