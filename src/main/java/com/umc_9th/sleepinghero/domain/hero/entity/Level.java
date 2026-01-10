package com.umc_9th.sleepinghero.domain.hero.entity;

package com.umc_9th.sleepinghero.domain.qna.entity;

import com.umc_9th.sleepinghero.domain.qna.entity.Question;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "level_rules")
@Builder
public class LevelRule {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private 

}
