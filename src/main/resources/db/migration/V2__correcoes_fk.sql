ALTER TABLE `users` DROP CONSTRAINT `fk_user_type_id`;
ALTER TABLE `users` DROP CONSTRAINT `fk_subscriptions_type_id`;
ALTER TABLE `user_payment_info` DROP CONSTRAINT `fk_user_id`;

ALTER TABLE `subscriptions_type` MODIFY COLUMN `subscriptions_type_id` BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE `user_payment_info` MODIFY COLUMN `user_id` BIGINT;
ALTER TABLE `user_type` MODIFY COLUMN `user_type_id` BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE `users` MODIFY COLUMN `users_id` BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE `users` MODIFY COLUMN `subscriptions_type_id` BIGINT;
ALTER TABLE `users` MODIFY COLUMN `user_type_id` BIGINT;

ALTER TABLE `users` ADD CONSTRAINT `fk_subscriptions_type_id` FOREIGN KEY (`subscriptions_type_id`) REFERENCES `subscriptions_type`(`subscriptions_type_id`);
ALTER TABLE `user_payment_info` ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `users`(`users_id`);
ALTER TABLE `users` ADD CONSTRAINT `fk_user_type_id` FOREIGN KEY (`user_type_id`) REFERENCES `user_type`(`user_type_id`);