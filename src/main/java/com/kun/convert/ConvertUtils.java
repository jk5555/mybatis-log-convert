package com.kun.convert;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


/**
 * @author kun.jiang@hand-china.com 2021-07-07 20:01
 */
public class ConvertUtils {



    public static boolean isSql(String text){
        if (text == null || text.length() <= 0) {
            return false;
        }

        if (!text.contains("?")) {
            return false;
        }

        final String tempText =  fixSql(text).toUpperCase(Locale.ROOT);
        boolean select = tempText.contains("SELECT") && tempText.contains("FROM");
        boolean insert = tempText.contains("INSERT INTO") && tempText.contains("VALUES");
        boolean delete = tempText.contains("DELETE") && tempText.contains("FROM");
        boolean update = tempText.contains("UPDATE") && tempText.contains("SET");

        return select || insert || delete || update;

    }


    public static boolean isParam(String text){
        if (text == null || text.length() <= 0) {
            return false;
        }

        final String tempText = fixParam(text);

        String[] split = tempText.split(",");
        for (String s : split) {
            s = s.trim();
            if (!(s.contains("(") && s.contains(")")) && !"null".equals(s)) {
                return false;
            }
            String type = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
            if (!FieldType.isNormalType(type)) {
                return false;
            }
        }

        return true;

    }

    @NotNull
    private static String fixSql(String text) {
        String paramFlag = ": ";
        if (text.contains(paramFlag)) {
            return text.substring(text.lastIndexOf(paramFlag) + 2).trim();
        } else {
            return text.trim();
        }
    }

    @NotNull
    private static String fixParam(String text) {
        String paramFlag = ": ";
        if (text.contains(paramFlag)) {
            return text.substring(text.lastIndexOf(paramFlag) + 2).trim();
        } else {
            return text.trim();
        }
    }


    public static String convert(String sql, String params){
        if (!isSql(sql) || !isParam(params)) {
            return null;
        }

        if (!sql.contains("?")) {
            return sql;
        }

        sql = fixSql(sql);
        String[] split = fixParam(params).split(",");
        for (String s : split) {
            String trim = s.trim();
            if ("null".equals(trim)) {
                int index = sql.indexOf("?");
                int end = index;
                char c;
                boolean isNullFalg = false;
                while (true){
                    index--;
                    c = sql.charAt(index);
                    if (c != ' ') {
                        if (c == '=') {
                            isNullFalg = true;
                        }
                        break;
                    }
                }
                if (isNullFalg) {
                    // 这个?不转义，还识别不了，这个有点坑
                    String sub = sql.substring(index, end) + "\\?";
                    sql = sql.replaceFirst(sub, " is null ");
                } else {
                    sql = sql.replaceFirst("\\?", trim);
                }
            } else {
                String param = trim.substring(0, trim.indexOf("("));
                String type = trim.substring(trim.indexOf("(") + 1, trim.indexOf(")"));
                String defaultStr = FieldType.getDefaultValue(type);
                String finalParam = defaultStr + param + defaultStr;
                sql = sql.replaceFirst("\\?", finalParam);
            }

            if (!sql.contains("?")) {
                break;
            }

        }

        return sql;

    }


    public static void main(String[] args) {
        String sql = "2021-07-07 20:44:21.809 DEBUG 1 --- [XNIO-3 task-226] [Z7Ma7duy] o.s.p.p.i.m.P.selectAsnLinePlan          ==>  Preparing: SELECT (CASE WHEN ? > spll.promise_delivery_date AND sph.confirmed_flag = 1 AND spl.cancelled_flag = 0 AND sph.publish_cancel_flag = 0 AND spll.closed_flag = 0 THEN (CASE spll.quantity WHEN spll.net_received_quantity THEN 0 ELSE 1 END) ELSE 0 END) AS overdue_flag, spll.po_line_id, spll.po_line_location_id, spll.po_header_id, spll.tenant_id, sph.company_id, CASE WHEN sph.supplier_company_id IS NULL THEN ssel.company_id ELSE sph.supplier_company_id END AS supplier_company_id, sph.supplier_tenant_id, CASE WHEN spl.item_code IS NULL THEN hit.item_code ELSE spl.item_code END AS item_code, CASE WHEN spl.item_name IS NULL THEN hit.item_name ELSE spl.item_name END AS item_name, hit.item_id, sph.po_num, sph.display_po_num, sph.release_num, sph.display_release_num, spl.display_line_num, spll.display_line_location_num, spll.line_location_num, spll.version_num, sph.po_type_id, spll.frozen_flag, sp.supplier_confirm_quantity AS quantity, sp.supplier_confirm_quantity - CASE WHEN spq.plan_occupied_quantity IS NULL THEN 0 ELSE spq.plan_occupied_quantity END AS can_asn_quantity, CASE WHEN spq.plan_net_received_quantity IS NULL THEN 0 ELSE spq.plan_net_received_quantity END AS net_received_quantity, CASE WHEN spq.plan_total_shipped_quantity IS NULL THEN 0 ELSE spq.plan_total_shipped_quantity END AS shipped_quantity, CASE WHEN spq.plan_occupied_quantity IS NULL THEN 0 ELSE spq.plan_occupied_quantity END AS occupied_quantity, spll.net_deliver_quantity, spll.invoiced_quantity, spll.on_the_way_quantity, spll.billed_quantity, spl.unit_price, spl.unit_price_batch, spl.entered_tax_included_price, sp.plan_date need_by_date, spll.promise_delivery_date, hit.exempt_inspection_flag, hit.common_name, hit.chart_code, spll.urgent_flag, spl.immed_shipped_flag, sph.company_name, hioe.organization_name AS inv_organization_name, spll.ship_to_third_party_address, spll.inv_inventory_id, hi.inventory_name, spll.inv_location_id, hl.location_name, hl.location_code, hpa.purchase_agent_name, spll.ship_to_third_party_name, spll.ship_to_third_party_contact, spll.remark AS line_location_remark, CASE WHEN sph.supplier_name IS NULL THEN ses.supplier_name ELSE sph.supplier_name END AS supplier_name, sess.supplier_site_name, spll.can_create_asn_flag, sph.purchase_org_id, sph.agent_id, spll.object_version_number, hpo.organization_name AS pur_organization_name, sph.supplier_id, sph.supplier_code AS supplier_num, sph.supplier_site_id, sph.ship_to_location_id, sph.ship_to_location_code, sph.ship_to_location_address, spll.inv_organization_id, sph.remark, sph.submitted_date, sph.ou_id, sph.supplier_company_name, sph.supplier_ou_id, sph.source_code, sph.external_system_code, sph.approved_by, sph.approved_date, sph.approved_remark, sph.erp_creation_date, sph.erp_last_update_date, sph.create_sync_date, sph.create_sync_response_msg, sph.create_sync_status, spl.uom_id, hul.uom_name, hu.uom_code, spll.cancelled_by, spll.cancelled_date, spll.closed_remark, spll.closed_by, spll.closed_date, spl.lot_num, spl.lot_validity_date, spll.ship_to_third_party_code, CASE WHEN spll.delivery_sync_status IS NOT NULL THEN spll.delivery_sync_status ELSE sph.delivery_sync_status END AS delivery_sync_status, CASE WHEN spll.delivery_sync_response_msg IS NOT NULL THEN spll.delivery_sync_response_msg ELSE sph.delivery_sync_response_msg END AS delivery_sync_response_msg, CASE WHEN spll.delivery_sync_date IS NOT NULL THEN spll.delivery_sync_date ELSE sph.delivery_sync_date END AS delivery_sync_date, sot.order_type_code, sott.order_type_name, sph.po_source_platform, spl.product_num, spl.product_name, spl.catalog_name, sp.plan_id, spl.pr_header_id, spl.pr_line_id, sc.financial_precision, sic.category_id, sict.category_name, hu.uom_precision FROM (SELECT * FROM sspl_plan DST__0 WHERE 1 = 1 AND 1 = 1 AND 1 = 1) sp JOIN sodr_po_line_location spll ON sp.po_line_location_id = spll.po_line_location_id JOIN (SELECT * FROM sodr_po_line DST__1 WHERE 1 = 1 AND 1 = 1) spl ON spl.po_line_id = spll.po_line_id JOIN sodr_po_header sph ON spll.po_header_id = sph.po_header_id LEFT JOIN sslm_ext_supplier_site sess ON sess.supplier_site_id = sph.supplier_site_id LEFT JOIN hpfm_purchase_organization hpo ON hpo.purchase_org_id = sph.purchase_org_id LEFT JOIN hpfm_inv_organization hioe ON hioe.organization_id = spll.inv_organization_id LEFT JOIN hpfm_inventory hi ON hi.inventory_id = spll.inv_inventory_id LEFT JOIN hpfm_location hl ON hl.location_id = spll.inv_location_id LEFT JOIN hpfm_purchase_agent hpa ON hpa.purchase_agent_id = sph.agent_id LEFT JOIN hpfm_company hc ON hc.company_id = sph.company_id LEFT JOIN (SELECT * FROM smdm_item DST__2 WHERE 1 = 1) hit ON hit.item_id = spl.item_id LEFT JOIN sslm_external_supplier ses ON ses.supplier_id = sph.supplier_id LEFT JOIN hpfm_company hsesc ON hsesc.company_id = ses.link_id LEFT JOIN spfm_supplier_ext_link ssel ON ssel.supplier_id = sph.supplier_id LEFT JOIN smdm_uom hu ON hu.uom_id = spl.uom_id LEFT JOIN smdm_uom_tl hul ON hu.uom_id = hul.uom_id AND hul.lang = ? LEFT JOIN sodr_order_type sot ON sot.order_type_id = sph.po_type_id LEFT JOIN sodr_order_type_tl sott ON sott.order_type_id = sot.order_type_id AND sott.lang = ? LEFT JOIN smdm_currency sc ON sc.tenant_id = ? AND sc.currency_code = spl.currency_code LEFT JOIN (SELECT sal.plan_id, SUM(CASE WHEN sal.cancelled_flag = 0 AND sal.closed_flag = 0 AND sah.asn_status IN ('NEW', 'SUBMITTED', 'APPROVED', 'REJECTED', 'SHIPPED', 'REVIEW_REJECTED') THEN sal.ship_quantity WHEN sal.cancelled_flag = 1 THEN sal.receive_quantity WHEN sal.closed_flag = 1 THEN sal.receive_quantity ELSE 0 END) AS plan_occupied_quantity, SUM(CASE WHEN sal.receive_status IN ('RECEIVE_PARTIAL', 'RECEIVE_COMPLETED') THEN sal.receive_quantity ELSE 0 END) AS plan_net_received_quantity, SUM(sal.ship_quantity) AS plan_total_shipped_quantity FROM (SELECT * FROM sinv_asn_line DST__3 WHERE 1 = 1) sal JOIN (SELECT * FROM sinv_asn_header DST__4 WHERE 1 = 1 AND 1 = 1 AND 1 = 1 AND 1 = 1 AND 1 = 1 AND 1 = 1 AND 1 = 1 AND 1 = 1 AND 1 = 1 AND 1 = 1) sah ON sal.asn_header_id = sah.asn_header_id WHERE sal.tenant_id = ? AND sal.plan_id IS NOT NULL GROUP BY sal.plan_id) spq ON spq.plan_id = sp.plan_id LEFT JOIN (SELECT * FROM sprm_pr_line DST__5 WHERE 1 = 1 AND 1 = 1 AND 1 = 1 AND 1 = 1 AND 1 = 1) sprl ON spl.pr_line_id = sprl.pr_line_id LEFT JOIN (SELECT * FROM sprm_pr_header DST__6 WHERE 1 = 1 AND 1 = 1 AND 1 = 1 AND 1 = 1 AND 1 = 1 AND 1 = 1 AND 1 = 1) sprh ON sprl.pr_header_id = sprh.pr_header_id LEFT JOIN smdm_item_category sic ON spl.category_id = sic.category_id LEFT JOIN smdm_item_category_tl sict ON sict.category_id = sic.category_id AND sict.lang = ? WHERE sph.confirmed_flag = 1 AND sph.status_code = 'PUBLISHED' AND spl.closed_flag = 0 AND sph.changing_flag != 1 AND spl.returned_flag != 1 AND sp.supplier_confirm_quantity > (CASE WHEN spq.plan_occupied_quantity IS NULL THEN 0 ELSE spq.plan_occupied_quantity END) AND sp.plan_status = 'CONFIRM' AND sph.tenant_id = ? AND spl.tenant_id = ? AND spll.tenant_id = ? AND sp.plan_id IN (?) AND spll.quantity - spll.occupied_quantity > 0 ORDER BY display_po_num ASC, display_line_num ASC, display_line_location_num ASC";
        String params = "2021-07-07 20:44:21.809 DEBUG 1 --- [XNIO-3 task-226] [Z7Ma7duy] o.s.p.p.i.m.P.selectAsnLinePlan          ==> Parameters: null, zh_CN(String), zh_CN(String), 35837(Long), 35837(Long), zh_CN(String), 35837(Long), 35837(Long), 35837(Long), 118079(Long)";
        String convert = convert(sql, params);
        System.out.println("convert = " + convert);
    }




}
