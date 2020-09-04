package kr.hs.dsm_scarfs.shank.entites.file.assignment;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignmentFile {

    @Id
    private Integer id;

    private String fileName;

    private String path;

}
