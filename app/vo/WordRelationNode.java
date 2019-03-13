package vo;

import com.fasterxml.jackson.databind.JsonNode;
import helper.CustomMapper;

import java.util.HashSet;
import java.util.Set;

public class WordRelationNode extends JsonEntity {

    public String actualWord;

    public String tokenziedWord;

    public Set<ChildRelNode> children;

    public Set<ParentRelNode> parents;

    public Set<ChildRelNode> decendents;

    public WordRelationNode() {
        children = new HashSet<>();
        parents = new HashSet<>();
        decendents = new HashSet<>();
    }

    public WordRelationNode(String actualWord, String  tokenziedWord) {
        this.actualWord = actualWord;
        this.tokenziedWord = tokenziedWord;
        children = new HashSet<>();
        parents = new HashSet<>();
        decendents = new HashSet<>();
    }

    public void setChildren(Set<ChildRelNode> children) {
        this.children = children;
    }

    public void setParents(Set<ParentRelNode> parents) {
        this.parents = parents;
    }

    public void setDecendents(Set<ChildRelNode> decendents) {
        this.decendents = decendents;
    }

    public Set<ChildRelNode> getChildren() {
        return children;
    }

    public Set<ParentRelNode> getParents() {
        return parents;
    }

    public Set<ChildRelNode> getDecendents() {
        return decendents;
    }

    @Override
    public JsonNode toJsonNode() {

        return CustomMapper.apiMapper.valueToTree(this);
    }
}
