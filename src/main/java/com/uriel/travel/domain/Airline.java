package com.uriel.travel.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Airline implements CodedEnum<String> {

    T1_AA("아메리칸항공", "AA"),
    T1_AC("에어카나다", "AC"),
    T1_AI("인도항공", "AI"),
    T1_AY("핀에어", "AY"),
    T1_BA("영국항공", "BA"),
    T1_NK("오케이항공", "NK"),
    T1_BR("에바항공", "BR"),
    T1_BX("에어부산", "BX"),
    T1_CA1("중국국제항공", "CA"),
    T1_CX("캐세이퍼시픽항공", "CX"),
    T1_CA2("중국남방항공", "CA"),
    T1_EK("에미레이트항공", "EK"),
    T1_ET("에티오피아항공", "ET"),
    T1_EY("에티하드항공", "EY"),
    T1_HA("하와이안항공", "HA"),
    T1_HB("그레이터베이항공", "HB"),
    T1_HO("길상항공", "HO"),
    T1_HX("홍콩항공", "HX"),
    T1_HY("우즈백항공", "HY"),
    T1_YP("에어프레미아", "YP"),
    T1_JL("일본항공", "JL"),
    T1_KA("드레곤에어", "KA"),
    T1_KC("에어아스타나", "KC"),
    T1_LH("루프트한자 독일항공", "LH"),
    T1_LO("폴란드항공", "LO"),
    T1_MH("말레이시아항공", "MH"),
    T1_MU("중국동방항공", "MU"),
    T1_NH("전일본공수", "NH"),
    T1_NX("에어마카오", "NX"),
    T1_NZ("에어뉴질랜드", "NZ"),
    T1_OM("몽골리안항공", "OM"),
    T1_OZ("아시아나항공", "OZ"),
    T1_PR("필리핀항공", "PR"),
    T1_QF("콴타스항공", "QF"),
    T1_QH("뱀부항공", "QH"),
    T1_QR("카타르항공", "QR"),
    T1_QV("라오에어라인", "QV"),
    T1_QW("청도항공", "QW"),
    T1_RS("에어서울", "RS"),
    T1_SC("산동항공", "SC"),
    T1_SQ("싱가폴항공", "SQ"),
    T1_SV("사우디아항공", "SV"),
    T1_TG("타이항공", "TG"),
    T1_TK("터키항공", "TK"),
    T1_TW("티웨이항공", "TW"),
    T1_TR("스쿠트항공", "TR"),
    T1_UA("유나이티드항공", "UA"),
    T1_UL("스리랑칸항공", "UL"),
    T1_UO("홍콩익스프레스", "UO"),
    T1_VJ("비엣젯항공", "VJ"),
    T1_VN("베트남항공", "VN"),
    T1_ZA("스카이앙코르항공", "ZA"),
    T1_ZE("이스타항공", "ZE"),
    T1_ZH("심천항공", "ZH"),
    T1_3U("사천항공", "3U"),
    T1_5J("세부퍼세픽항공", "5J"),
    T1_7C("제주항공", "7C"),
    T2_AF("에어프랑스", "AF"),
    T2_AM("아에로멕시코", "AM"),
    T2_CI("중화항공", "CI"),
    T2_DL("텔타항공", "DL"),
    T2_GA("가두다 인도네시아항공", "GA"),
    T2_KE("대한항공", "KE"),
    T2_KL("케이엘엠 네덜란드항공", "GA"),
    T2_LJ("진에어", "GA"),
    T2_MF("중국하문항공", "GA"),
    T2_SU("러시아항공", "GA");

    private final String viewName;
    private final String code;

    @JsonCreator
    public static Airline from(String sub) {
        for (Airline airline: Airline.values()) {
            if (airline.getViewName().equals(sub)) {
                return airline;
            }
        }
        return null;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<Airline, String> {
        public Converter() {
            super(Airline.class);
        }
    }
}
