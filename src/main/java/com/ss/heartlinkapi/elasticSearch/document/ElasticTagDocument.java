package com.ss.heartlinkapi.elasticSearch.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "tag_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElasticTagDocument {
    @Id
    @Field(type = FieldType.Keyword)
    private String documentId;
    @Field(type = FieldType.Keyword)
    private Long tagId;
    @Field(type = FieldType.Text)
    private String tagName;
}
