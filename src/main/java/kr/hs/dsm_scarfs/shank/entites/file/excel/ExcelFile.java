package kr.hs.dsm_scarfs.shank.entites.file.excel;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExcelFile {

    @Id
    private Integer id;

    private Integer assignmentId;

    private String fileName;

    private String path;

}
