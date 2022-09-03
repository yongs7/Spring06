package com.example.intermediate.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "date_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Date date;

    @Column
    private String content;

    @Column
    private int pay;

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }
}
